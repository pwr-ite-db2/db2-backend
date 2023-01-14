package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.*;
import com.example.databasedemo2.entitymanagement.repositories.ArticleRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.ArticlesInEditViewRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.MainPageViewRepository;
import com.example.databasedemo2.entitymanagement.views.MainPageView;
import com.example.databasedemo2.exceptions.custom.AuthorizationException;
import com.example.databasedemo2.exceptions.custom.ResourceNotFoundException;
import com.example.databasedemo2.security.ArticleChangeValidator;
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
    private final ArticleChangeValidator validator;

    @Autowired
    public ArticleService(ArticleRepository repository, MainPageViewRepository mainPageViewRepository,
                          UserAuthenticationInfoImpl userInfo, ArticleStatusService articleStatusService,
                          ChangeService changeService, TagService tagService, ArticlesInEditViewRepository inEditViewRepository) {
        super(repository);
        this.mainPageViewRepository = mainPageViewRepository;
        this.userInfo = userInfo;
        this.articleStatusService = articleStatusService;
        this.changeService = changeService;
        this.tagService = tagService;
        this.validator = new ArticleChangeValidator(inEditViewRepository);
    }



    // Overridden methods from the base class

    // doesn't allow unauthorized users to access unpublished articles
    @Override
    public List<Article> getAll(Map<String, String> params) {
        if ((userInfo.isAnonymousUser() || userInfo.isClient())
                && (params.isEmpty() || !params.getOrDefault("status", "").equals("4"))) {

            params.put("status", "4");
            params.put("sum", "false");
        }

        return super.getAll(params);
    }

    // handles article changes by author and editor
    @Override
    public Article addOrUpdate(Article entity, Map<String, String> params) throws EntityNotFoundException, AuthorizationException {
        // get user making changes to the article
        User currentUser = userInfo.getAuthenticationInfo();

        // Get all tags and create a new map
        if (entity.getTags() != null && !entity.getTags().isEmpty()) {
            Set<Tag> tagsDb = new HashSet<>(tagService.getAll(null));
            Map<String, Tag> nameToTagMap = tagsDb.stream().collect(Collectors.toMap(Tag::getName, tag -> tag));

            // Map and update tags
            Set<Tag> tagsPassedEntity = entity.getTags()
                    .stream()
                    .map(tag -> nameToTagMap.getOrDefault(tag.getName(), Tag.builder().name(tag.getName()).build())).
                    collect(Collectors.toSet());

            entity.setTags(tagsPassedEntity);
        }
        else
            entity.setTags(Collections.emptySet());

        if (entity.getTitle() == null)
            entity.setTitle("");

        // new article
        if (entity.getId() == 0) {

            // prevent editor from creating new article
            if (userInfo.isEditor())
                throw new AuthorizationException();

            // initialize new article and override values by author
            // set author as current user
            entity.setAuthor(currentUser);

            // set default status for new articles
            if (!entity.isAllowStatusChange()) {
                ArticleStatus defaultStatus = articleStatusService.getDefaultStatus();
                entity.setArticleStatus(defaultStatus);
            }

            // new article shouldn't have any comments
            if (entity.getComments() != null)
                entity.getComments().clear();

            // new article shouldn't have any changes
            if (entity.getChangeHistory() != null)
                entity.getChangeHistory().clear();

            entity.setRemovedAt(null);
            entity.setViewCount(0);

            // new article with new tags -> persist article entity first
            if (entity.getTags().stream().anyMatch(tag -> tag.getId() == 0) ||
                    entity.getChapters() != null) {

                List<Chapter> chaptersCopy = null;

                // new article with chapters (chapters need article id as fk)
                if (entity.getChapters() != null) {
                    chaptersCopy = new LinkedList<>(entity.getChapters());
                    entity.setChapters(null);
                }

                entity = repository.save(entity);

                entity.setChapters(chaptersCopy);
            }
        }
        else if (entity.getId() != 0) {
            // throws ResourceNotFoundException if record is not present -> prevent from creating new records
            Article articleOld = repository.findById(entity.getId()).orElseThrow(ResourceNotFoundException::new);

            validator.validateChanges(articleOld, entity, currentUser); // throws AuthorizationException exception if made changes are invalid

            if (!entity.isAllowStatusChange())
                entity.setArticleStatus(articleOld.getArticleStatus());

            entity.setAuthor(articleOld.getAuthor());
            entity.setAdultContent(articleOld.isAdultContent());
            entity.setRemovedAt(articleOld.getRemovedAt());
            entity.setViewCount(articleOld.getViewCount());

            // both comments and chapters are enabled for orphan removal, so they need to be restored

            // restore comments
            entity.setComments(articleOld.getComments());

            // restore chapters
            List<Chapter> chaptersCopy = Collections.emptyList();

            // if chapters are present make a copy
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

    @Override
    public boolean deleteById(Integer articleId) throws ResourceNotFoundException, AuthorizationException {
        Article article = super.getById(articleId);
        User currentUser = userInfo.getAuthenticationInfo();

        if (validator.isValidForDeletion(article, currentUser)) {
            return super.deleteById(articleId);

        } else
            throw new AuthorizationException();
    }


    // Custom articles methods

    public List<MainPageView> getMainPageContent(String numOfDays) {
        return getMainPageContentFromLastDays(Integer.parseInt(numOfDays));
    }

    private List<MainPageView> getMainPageContentFromLastDays(int numOfDays) {
        long daysInMillis = 1000L * 60 * 60 * 24 * numOfDays;
        return mainPageViewRepository.findAllByReleaseDateAfter(new Date(System.currentTimeMillis() - daysInMillis));
    }

    public Article pickArticleForEditing(int articleId) throws ResourceNotFoundException {
        Article article = getById(articleId);   // throws ResourceNotFoundException if article is not found

        if (article.getArticleStatus().getName().equals("OCZEKUJĄCY NA REDAKCJĘ")
                || article.getArticleStatus().getName().equals("WYCOFANY")) {

            ArticleStatus newStatus = articleStatusService.findByName("REDAGOWANY");
            User currentUser = userInfo.getAuthenticationInfo();

            article.setArticleStatus(newStatus);
            article = repository.save(article);
            logChanges(articleId, currentUser, "Artykuł pobrany do redakcji", newStatus);
            return article;
        }

        throw new RuntimeException("Cannot edit article of id " + article.getId() + "!");
    }

    public Article publishArticle(Article article) {
        ArticleStatus oldArticleStatus = repository.getReferenceById(article.getId()).getArticleStatus();
        article.setArticleStatus(oldArticleStatus);

        if (validator.isValidForPublishing(article)) {
            ArticleStatus newStatus = articleStatusService.findByName("OPUBLIKOWANY");
            article.setArticleStatus(newStatus);
            article.setRemovedAt(null);

            article.setAllowStatusChange(true);
            article = addOrUpdate(article, Map.of("note", "Artykuł opublikowany na portal"));
            return article;
        }

        throw new RuntimeException("Cannot publish article of id " + article.getId() + "!");
    }

    public Article submitArticleForEditing(Article article) throws ResourceNotFoundException, AuthorizationException {
        ArticleStatus oldArticleStatus = repository.getReferenceById(article.getId()).getArticleStatus();
        System.out.println("oldArticleStatus = " + oldArticleStatus);
        article.setArticleStatus(oldArticleStatus);

        if (validator.isValidForEditing(article)) {
            ArticleStatus newStatus = articleStatusService.findByName("OCZEKUJĄCY NA REDAKCJĘ");
            article.setArticleStatus(newStatus);

            article.setAllowStatusChange(true);
            article = addOrUpdate(article, Map.of("note", "Artykuł oddany do redakcji"));
            return article;
        }

        throw new RuntimeException("Cannot submit article of id " + article.getId() + " for editing!");
    }

    public Article rollbackArticle(int articleId) throws ResourceNotFoundException, AuthorizationException {
        Article article = getById(articleId);   // throws ResourceNotFoundException if article is not found
        User currentUser = userInfo.getAuthenticationInfo();

        if (validator.isValidForRollback(article, currentUser)) {
            ArticleStatus newStatus = articleStatusService.findByName("WYCOFANY");

            article.setArticleStatus(newStatus);
            article.setRemovedAt(new Date());
            article = repository.save(article);
            logChanges(articleId, currentUser, "Artykuł wycofany", newStatus);
            return article;
        }

        throw new RuntimeException("Cannot submit article of id " + article.getId() + " for rollback!");
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



    // Comments methods

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
        User currentUser = userInfo.getAuthenticationInfo();


        // check if article is published
        if ((userInfo.isAnonymousUser() || userInfo.isClient())
                && (!article.getArticleStatus().getName().equals("OPUBLIKOWANY"))) {

            throw new AuthorizationException();
        }

        if (comment.getId() == 0) {
            comment.setUser(currentUser);
            comment.setCreatedAt(new Date());
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



    // Changes methods
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