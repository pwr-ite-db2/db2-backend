package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.businesslogic.services.ArticleService;
import com.example.databasedemo2.dataaccess.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<Article> getArticles() {
        return articleService.getAllArticles();
    }

    @PutMapping
    public Article createOrUpdateArticle(@RequestBody Article article) {
        return articleService.addOrUpdateArticle(article);
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable("id") int articleId) {
        return articleService.getArticleById(articleId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteArticleById(@PathVariable("id") int articleId) {
        return articleService.deleteArticleById(articleId);
    }
}
