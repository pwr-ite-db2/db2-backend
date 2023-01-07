package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.*;
import com.example.databasedemo2.entitymanagement.repositories.ArticleRepository;
import com.example.databasedemo2.entitymanagement.repositories.ArticleStatusRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.MainPageViewRepository;
import com.example.databasedemo2.entitymanagement.views.MainPageView;
import com.example.databasedemo2.exceptions.custom.ResourceNotFoundException;
import com.example.databasedemo2.security.UserAuthenticationInfoImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class ArticleService extends BaseService<Article, Integer> {

    private final MainPageViewRepository mainPageViewRepository;

    private final UserAuthenticationInfoImpl userInfo;

    private final ArticleStatusRepository articleStatusRepository;

    private final ChangeService changeService;

    private static final Map<String, Function<Object, List<Article>>> paramToFunctionMap = new HashMap<>();

    @Autowired
    public ArticleService(JpaRepository<Article, Integer> repository, MainPageViewRepository mainPageViewRepository,
                          UserAuthenticationInfoImpl userInfo, ArticleStatusRepository articleStatusRepository,
                          ChangeService changeService) {
        super(repository);
        this.mainPageViewRepository = mainPageViewRepository;
        this.userInfo = userInfo;
        this.articleStatusRepository = articleStatusRepository;
        this.changeService = changeService;
    }

    @Override
    public List<Article> getAll(Map<String, Object> params) {
//        System.out.println("params = " + params);
        System.out.println(params.get("tag_id"));
        return super.getAll(params);
    }

    public List<MainPageView> getMainPageContent() {
        return mainPageViewRepository.findAll();
    }

    public Article pickArticleForEditing(int articleId) throws ResourceNotFoundException {
        Article article = getById(articleId);
        ArticleStatus newStatus = articleStatusRepository.findByName("REDAGOWANY").orElseThrow(ResourceNotFoundException::new);
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
        ArticleStatus newStatus = articleStatusRepository.findByName("OPUBLIKOWANY").orElseThrow(ResourceNotFoundException::new);

        if (article.getArticleStatus().getName().equals("REDAGOWANY")
                || article.getArticleStatus().getName().equals("WYCOFANY")) {

            article.setArticleStatus(newStatus);
            article = addOrUpdate(article, Map.of("note", "Artykuł opublikowany na portal"));
            return article;
        }

        throw new RuntimeException("Cannot publish article of id " + article.getId() + "!");
    }

    public Article submitArticleForEditing(Article article) {
        ArticleStatus newStatus = articleStatusRepository.findByName("OCZEKUJĄCY NA REDAKCJĘ").orElseThrow(ResourceNotFoundException::new);

        if (article.getArticleStatus().getName().equals("UTWORZONY")) {
            article.setArticleStatus(newStatus);
            article = addOrUpdate(article, Map.of("note", "Artykuł oddany do redakcji"));
            return article;
        }

        throw new RuntimeException("Cannot submit article of id " + article.getId() + " for editing!");
    }

    public Article rollbackArticle(int articleId) {
        Article article = getById(articleId);
        ArticleStatus newStatus = articleStatusRepository.findByName("WYCOFANY").orElseThrow(ResourceNotFoundException::new);
        User currentUser = userInfo.getAuthenticationInfo();

        article.setArticleStatus(newStatus);
        article = repository.save(article);
        logChanges(articleId, currentUser, "Artykuł wycofany", newStatus);
        return article;
    }

    @Override
    public Article addOrUpdate(Article entity, Map<String, Object> params) throws EntityNotFoundException, ResourceNotFoundException {
        // get user making changes to the article
        User currentUser = userInfo.getAuthenticationInfo();

        // new article
        if (entity.getId() == 0) {
            // set author as current user
            if (entity.getAuthor() == null) {
                entity.setAuthor(currentUser);
            }

            // set default status for new articles
            if (entity.getArticleStatus() == null) {
                ArticleStatus defaultStatus = articleStatusRepository.findByName("UTWORZONY").orElseThrow(ResourceNotFoundException::new);
                entity.setArticleStatus(defaultStatus);
            }

            // new article with chapters
            if (entity.getChapters() != null) {
                List<Chapter> chaptersCopy = new LinkedList<>(entity.getChapters());
                entity.setChapters(null);

                entity = repository.save(entity);

                entity.setChapters(chaptersCopy);
            }
        }
        else if (entity.getId() != 0) {

            if (entity.getComments() == null) {
                // restore comments
                List<Comment> comments = repository.findById(entity.getId()).orElseThrow(ResourceNotFoundException::new).getComments();
                entity.setComments(comments);
            }

        }

        for (Chapter chapter : entity.getChapters()) {
            chapter.setArticle(entity);
        }

        String changesNote = (String) params.get("note");
        logChanges(entity.getId(), currentUser, changesNote, entity.getArticleStatus());
        return super.addOrUpdate(entity, params);
    }

    public List<Comment> getAllCommentsByArticleId(int articleId) throws ResourceNotFoundException {
        Article article = repository.findById(articleId).orElseThrow(ResourceNotFoundException::new);

        List<Comment> comments = article.getComments();
        List<Comment> commentsCopy = new LinkedList<>();

        for (Comment comment : comments) {
            if (comment.getParentComment() == null)
                commentsCopy.add(comment);
        }

        return commentsCopy;
    }

    public Comment getCommentByArticleIdAndCommentId(int articleId, int commentId) throws ResourceNotFoundException {
        Article article = repository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        List<Comment> articleComments = article.getComments();

        for (Comment c : articleComments) {
            if (c.getId() == commentId)
                return c;
        }

        throw new ResourceNotFoundException();
    }

    public Comment addOrUpdateComment(int articleId, Comment comment) {
        Article article = repository.findById(articleId).orElseThrow(ResourceNotFoundException::new);

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

    private void populate() {
        paramToFunctionMap.put("category", o ->
            ((ArticleRepository) repository).findAllByCategory_Name((String) o)
        );

        paramToFunctionMap.put("author", o ->
                ((ArticleRepository) repository).findAllByAuthor_Name((String) o)
        );
    }
}