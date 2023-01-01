package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.Article;
import com.example.databasedemo2.dataaccess.entities.Chapter;
import com.example.databasedemo2.dataaccess.entities.Comment;
import com.example.databasedemo2.dataaccess.repositories.readonly.ArticlesInEditViewRepository;
import com.example.databasedemo2.dataaccess.repositories.readonly.ArticlesInMakingViewRepository;
import com.example.databasedemo2.dataaccess.repositories.readonly.MainPageViewRepository;
import com.example.databasedemo2.dataaccess.views.MainPageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleService extends BaseService<Article, Integer> {
    private final ArticlesInEditViewRepository inEditViewRepository;
    private final ArticlesInMakingViewRepository inMakingViewRepository;

    private final MainPageViewRepository mainPageViewRepository;

    @Autowired
    public ArticleService(JpaRepository<Article, Integer> repository, ArticlesInEditViewRepository inEditViewRepository,
                          ArticlesInMakingViewRepository inMakingViewRepository, MainPageViewRepository mainPageViewRepository) {
        super(repository);
        this.inEditViewRepository = inEditViewRepository;
        this.inMakingViewRepository = inMakingViewRepository;
        this.mainPageViewRepository = mainPageViewRepository;
    }

    public List<MainPageView> getMainPageContent() {
        return mainPageViewRepository.findAll();
    }

    @Override
    public Article addOrUpdate(Article entity) {
        // new article with chapters
        if (entity.getId() == 0 && entity.getChapters() != null) {
            List<Chapter> chaptersCopy = new LinkedList<>(entity.getChapters());
            entity.setChapters(null);

            entity = super.addOrUpdate(entity);

            entity.setChapters(chaptersCopy);

            for (Chapter chapter : entity.getChapters()) {
                chapter.setArticle(entity);
            }

        } else if (entity.getId() != 0) {
            List<Comment> comments = repository.findById(entity.getId()).orElseThrow().getComments();
            entity.setComments(comments);
        }

        return super.addOrUpdate(entity);
    }

    public List<Comment> getAllCommentsByArticleId(int articleId) {
        Article article = repository.findById(articleId).orElseThrow();

        List<Comment> comments = article.getComments();
        List<Comment> commentsCopy = new LinkedList<>();

        for (Comment comment : comments) {
            if (comment.getParentComment() == null)
                commentsCopy.add(comment);
        }

        return commentsCopy;
    }

    public Comment getCommentByArticleIdAndCommentId(int articleId, int commentId) {
        Article article = repository.findById(articleId).orElseThrow();
        List<Comment> articleComments = article.getComments();

        for (Comment c : articleComments) {
            if (c.getId() == commentId)
                return c;
        }

        return null;
    }

    public Comment addOrUpdateComment(int articleId, Comment comment) {
        Article article = repository.getReferenceById(articleId);
        comment.setArticle(article);

        if (comment.getId() != 0)
            comment.setUpdatedAt(new Date());

        article.getComments().add(comment);
        article = repository.save(article);
        return article.getComments().get(article.getComments().size() - 1);
    }

    public boolean deleteCommentById(int articleId, int commentId) {
        Article article = repository.getReferenceById(articleId);
        List<Comment> comments = article.getComments();
        return comments.removeIf(comment -> (comment.getId() == commentId));
    }

    @Override
    protected void handle() {

    }
}