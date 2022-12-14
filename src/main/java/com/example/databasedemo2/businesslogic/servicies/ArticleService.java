package com.example.databasedemo2.businesslogic.servicies;

import com.example.databasedemo2.dataaccess.entities.Article;
import com.example.databasedemo2.dataaccess.entities.Change;
import com.example.databasedemo2.dataaccess.entities.User;
import com.example.databasedemo2.dataaccess.repositories.ArticleRepository;
import com.example.databasedemo2.dataaccess.repositories.ChangesRepository;
import com.example.databasedemo2.dataaccess.repositories.UserRepository;
import com.example.databasedemo2.dataaccess.repositories.readonly.ArticlesInEditViewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final ArticlesInEditViewRepository articlesInEditViewRepository;

    private final UserRepository userRepository;

    private final ChangesRepository changesRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, ArticlesInEditViewRepository articlesInEditViewRepository, UserRepository userRepository, ChangesRepository changesRepository) {
        this.articleRepository = articleRepository;
        this.articlesInEditViewRepository = articlesInEditViewRepository;
        this.userRepository = userRepository;
        this.changesRepository = changesRepository;
    }

    public Set<Article> articlesByUser(int userId) {
        User user = userRepository.getReferenceById(userId);

        if (!user.getRole().getName().equals("AUTOR"))
            return null;

        List<Change> changes = user.getChangesByUser();
        Set<Article> articles = new TreeSet<>(Comparator.comparingInt(Article::getId));

        for (Change change : changes) {
            articles.add(change.getArticle());
        }

        return articles;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article addNewArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article getArticleById(int articleId) {
        return articleRepository.getReferenceById(articleId);
    }

    public Article updateArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }
}