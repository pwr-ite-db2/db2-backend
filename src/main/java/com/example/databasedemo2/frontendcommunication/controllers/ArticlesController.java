package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.dataaccess.entities.Article;
import com.example.databasedemo2.dataaccess.entities.ArticleStatus;
import com.example.databasedemo2.dataaccess.repositories.ArticleRepository;
import com.example.databasedemo2.dataaccess.repositories.ArticleStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticlesController {
    private final ArticleRepository articleRepository;
    private final ArticleStatusRepository articleStatusRepository;

    @Autowired
    public ArticlesController(ArticleRepository articleRepository, ArticleStatusRepository articleStatusRepository) {
        this.articleRepository = articleRepository;
        this.articleStatusRepository = articleStatusRepository;
    }

    @GetMapping("/test")
    public List<ArticleStatus> test() {
        List<ArticleStatus> statuses = articleStatusRepository.findAll();
//        statuses.forEach(System.out::println);
        return statuses;
    }

    @GetMapping("/articles")
    public List<Article> getArticles() {
        return articleRepository.findAll();
    }
}
