package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.*;
import com.example.databasedemo2.entitymanagement.repositories.ArticleRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.MainPageViewRepository;
import com.example.databasedemo2.entitymanagement.views.MainPageView;
import com.example.databasedemo2.exceptions.custom.AuthorizationException;
import com.example.databasedemo2.exceptions.custom.ResourceNotFoundException;
import com.example.databasedemo2.security.UserAuthenticationInfoImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService extends BaseService<Article, Integer> {

    private final MainPageViewRepository mainPageViewRepository;

    private final UserAuthenticationInfoImpl userInfo;

    private final ArticleStatusService articleStatusService;

    private final ChangeService changeService;
    private final TagService tagService;

    @Autowired
    public ArticleService(ArticleRepository repository, MainPageViewRepository mainPageViewRepository,
                          UserAuthenticationInfoImpl userInfo, ArticleStatusService articleStatusService,
                          ChangeService changeService, TagService tagService) {
        super(repository);
        this.mainPageViewRepository = mainPageViewRepository;
        this.userInfo = userInfo;
        this.articleStatusService = articleStatusService;
        this.changeService = changeService;
        this.tagService = tagService;
    }

    public List<MainPageView> getMainPageContent(String numOfDays) {
        System.out.println(numOfDays);
        return getMainPageContentFromLastDays(Integer.parseInt(numOfDays));
    }

    private List<MainPageView> getMainPageContentFromLastDays(int numOfDays) {
        long daysInMillis = 1000L * 60 * 60 * 24 * numOfDays;
        System.out.println(new Date(System.currentTimeMillis() - daysInMillis));
        return mainPageViewRepository.findAllByReleaseDateAfter(new Date(System.currentTimeMillis() - daysInMillis));
    }

    public Article pickArticleForEditing(int articleId) throws ResourceNotFoundException {
        Article article = getById(articleId);
        ArticleStatus newStatus = articleStatusService.findByName("REDAGOWANY");
        User currentUser = userInfo.getAuthenticationInfo();

        if (article.getArticleStatus().getName().equals("OCZEKUJĄCY NA REDAKCJĘ")
                || article.getArticleStatus().getName().equals("WYCOFANY")) {

            article.setArticleStatus(newStatus);
            article = repository.save(article);
            logChanges(articleId, currentUser, "Artykuł pobrany do redakcji", newStatus);
            return article;
        }

        throw new RuntimeException("Cannot edit article of id " + article.getId() + "!");
    }

    public Article publishArticle(Article article) {
        ArticleStatus newStatus = articleStatusService.findByName("OPUBLIKOWANY");

        if (article.getArticleStatus().getName().equals("REDAGOWANY")
                || article.getArticleStatus().getName().equals("WYCOFANY")) {

            article.setArticleStatus(newStatus);
            article.setRemovedAt(null);
            article = addOrUpdate(article, Map.of("note", "Artykuł opublikowany na portal"));
            return article;
        }

        throw new RuntimeException("Cannot publish article of id " + article.getId() + "!");
    }

    public Article submitArticleForEditing(Article article) {
        ArticleStatus newStatus = articleStatusService.findByName("OCZEKUJĄCY NA REDAKCJĘ");

        // if status == null -> new article, submitted without previous save
        if (article.getArticleStatus() == null || article.getArticleStatus().getName().equals("UTWORZONY")) {
            article.setArticleStatus(newStatus);
            article = addOrUpdate(article, Map.of("note", "Artykuł oddany do redakcji"));
            return article;
        }

        throw new RuntimeException("Cannot submit article of id " + article.getId() + " for editing!");
    }

    public Article rollbackArticle(int articleId) {
        Article article = getById(articleId);
        ArticleStatus newStatus = articleStatusService.findByName("WYCOFANY");
        User currentUser = userInfo.getAuthenticationInfo();

        article.setArticleStatus(newStatus);
        article.setRemovedAt(new Date());
        article = repository.save(article);
        logChanges(articleId, currentUser, "Artykuł wycofany", newStatus);
        return article;
    }

    @Override
    public List<Article> getAll(Map<String, String> params) {
        if ((userInfo.isAnonymousUser() || userInfo.isClient())
                && (params.isEmpty() || !params.getOrDefault("status", "").equals("4"))) {

            params.put("status", "4");
            params.put("sum", "false");
        }

        return super.getAll(params);
    }

    public Article readArticle(int articleId) throws ResourceNotFoundException, AuthorizationException {
        Article article = super.getById(articleId);

        // check if article is published
        if ((userInfo.isAnonymousUser() || userInfo.isClient())
                && (!article.getArticleStatus().getName().equals("OPUBLIKOWANY"))) {

            throw new AuthorizationException();
        }

        int viewCount = article.getViewCount();
        article.setViewCount(viewCount + 1);
        return repository.save(article);
    }

    // handles article changes
    @Override
    public Article addOrUpdate(Article entity, Map<String, String> params) throws EntityNotFoundException {
        // get user making changes to the article
        User currentUser = userInfo.getAuthenticationInfo();

        // Get all tags and create a new map
        Set<Tag> tagsDb = new HashSet<>(tagService.getAll(null));
        Map<String, Tag> nameToTagMap = tagsDb.stream().collect(Collectors.toMap(Tag::getName, tag -> tag));

        // Map and update tags
        Set<Tag> tagsPassedEntity = entity.getTags()
                .stream()
                .map(tag -> nameToTagMap.getOrDefault(tag.getName(), Tag.builder().name(tag.getName()).build())).
                collect(Collectors.toSet());

        entity.setTags(tagsPassedEntity);

        // new article
        if (entity.getId() == 0) {
            // set author as current user
            if (entity.getAuthor() == null) {
                entity.setAuthor(currentUser);
            }

            // set default status for new articles
            if (entity.getArticleStatus() == null) {
                ArticleStatus defaultStatus = articleStatusService.getDefaultStatus();
                entity.setArticleStatus(defaultStatus);
            }

            // new article with new tags -> persist article entity first
            if (entity.getTags().stream().anyMatch(tag -> tag.getId() == 0)) {
                List<Chapter> chaptersCopy = null;

                if (entity.getChapters() != null) {
                    chaptersCopy = new LinkedList<>(entity.getChapters());
                    entity.setChapters(null);
                }

                entity = repository.save(entity);

                entity.setChapters(chaptersCopy);
            }
        }
        // both comments and chapters are enabled for orphan removal, so they need to be restored
        else if (entity.getId() != 0) {

            if (entity.getComments() == null) {
                // restore comments
                Article articleOld = repository.findById(entity.getId()).orElseThrow(ResourceNotFoundException::new);
                List<Comment> comments = articleOld.getComments();
                entity.setComments(comments);
            }

            List<Chapter> chaptersCopy = Collections.emptyList();

            if (entity.getChapters() != null)
                chaptersCopy = new LinkedList<>(entity.getChapters());

            // delete all chapters and flush the changes
            entity.getChapters().clear();
            entity = repository.saveAndFlush(entity);

            entity.getChapters().addAll(chaptersCopy);
        }

        if (entity.getChapters() != null) {
            for (Chapter chapter : entity.getChapters()) {
                chapter.setArticle(entity);
            }
        }

        entity = super.addOrUpdate(entity, params);
        String changesNote = params.get("note");
        logChanges(entity.getId(), currentUser, changesNote, entity.getArticleStatus());
        return entity;
    }

    public List<Comment> getAllCommentsByArticleId(int articleId) throws ResourceNotFoundException, AuthorizationException {
        Article article = repository.findById(articleId).orElseThrow(ResourceNotFoundException::new);

        // check if article is published
        if ((userInfo.isAnonymousUser() || userInfo.isClient())
                && (!article.getArticleStatus().getName().equals("OPUBLIKOWANY"))) {

            throw new AuthorizationException();
        }

        List<Comment> comments = article.getComments();
        List<Comment> commentsCopy = new LinkedList<>();

        for (Comment comment : comments) {
            if (comment.getParentComment() == null)
                commentsCopy.add(comment);
        }

        return commentsCopy;
    }

    public Comment getCommentByArticleIdAndCommentId(int articleId, int commentId) throws ResourceNotFoundException, AuthorizationException {
        Article article = repository.findById(articleId).orElseThrow(ResourceNotFoundException::new);

        // check if article is published
        if ((userInfo.isAnonymousUser() || userInfo.isClient())
                && (!article.getArticleStatus().getName().equals("OPUBLIKOWANY"))) {

            throw new AuthorizationException();
        }

        List<Comment> articleComments = article.getComments();

        for (Comment c : articleComments) {
            if (c.getId() == commentId)
                return c;
        }

        throw new ResourceNotFoundException();
    }

    public Comment addOrUpdateComment(int articleId, Comment comment) throws ResourceNotFoundException, AuthorizationException {
        Article article = repository.findById(articleId).orElseThrow(ResourceNotFoundException::new);

        // check if article is published
        if ((userInfo.isAnonymousUser() || userInfo.isClient())
                && (!article.getArticleStatus().getName().equals("OPUBLIKOWANY"))) {

            throw new AuthorizationException();
        }

        if (comment.getId() == 0) {
            User author = userInfo.getAuthenticationInfo();
            comment.setUser(author);
        } else {
            comment.setUpdatedAt(new Date());
        }

        comment.setArticle(article);

        article.getComments().add(comment);
        article = repository.save(article);
        return article.getComments().get(article.getComments().size() - 1);
    }

    public boolean deleteCommentById(int articleId, int commentId) {
        Article article = repository.getReferenceById(articleId);
        List<Comment> comments = article.getComments();
        return comments.removeIf(comment -> (comment.getId() == commentId));
    }

    public List<Change> getChanges(Map<String, String> params) {
        return changeService.getAll(params);
    }

    private void logChanges(int articleId, User user, String note, ArticleStatus status) {
        Change change = Change.builder()
                .article(Article.builder().id(articleId).build())
                .date(new Date())
                .user(user)
                .notes(note)
                .statusAfterChanges(status)
                .version((short) (changeService.getLatestVersionNumberByArticleId(articleId) + 1))
                .build();

        changeService.addOrUpdate(change, Collections.emptyMap());
    }
}