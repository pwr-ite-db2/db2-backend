package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Article;
import com.example.databasedemo2.entitymanagement.entities.Chapter;
import com.example.databasedemo2.entitymanagement.entities.Comment;
import com.example.databasedemo2.entitymanagement.entities.User;
import com.example.databasedemo2.entitymanagement.repositories.readonly.MainPageViewRepository;
import com.example.databasedemo2.entitymanagement.views.MainPageView;
import com.example.databasedemo2.security.UserAuthenticationInfoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleService extends BaseService<Article, Integer> {

    private final MainPageViewRepository mainPageViewRepository;

    private final UserAuthenticationInfoImpl userInfo;

    @Autowired
    public ArticleService(JpaRepository<Article, Integer> repository, MainPageViewRepository mainPageViewRepository,
                          UserAuthenticationInfoImpl userInfo) {
        super(repository);
        this.mainPageViewRepository = mainPageViewRepository;
        this.userInfo = userInfo;
    }

    public List<MainPageView> getMainPageContent() {
        return mainPageViewRepository.findAll();
    }

    @Override
    public Article addOrUpdate(Article entity) {
        // new article
        if (entity.getId() == 0) {
            // set author as current user
            User author = userInfo.getAuthenticationInfo();
            entity.setAuthor(author);

            // new article with chapters
            if (entity.getChapters() != null) {
                List<Chapter> chaptersCopy = new LinkedList<>(entity.getChapters());
                entity.setChapters(null);

                entity = super.addOrUpdate(entity);

                entity.setChapters(chaptersCopy);
            }
        }
        else if (entity.getId() != 0) {
            List<Comment> comments = repository.findById(entity.getId()).orElseThrow().getComments();
            entity.setComments(comments);
        }

        for (Chapter chapter : entity.getChapters()) {
            chapter.setArticle(entity);
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
}