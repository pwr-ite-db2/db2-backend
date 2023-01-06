package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.anotations.isAdmin;
import com.example.databasedemo2.anotations.isEmployee;
import com.example.databasedemo2.anotations.isLoggedIn;
import com.example.databasedemo2.entitymanagement.entities.Article;
import com.example.databasedemo2.entitymanagement.entities.Comment;
import com.example.databasedemo2.entitymanagement.services.ArticleService;
import com.example.databasedemo2.entitymanagement.views.MainPageView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/main-page")
    public List<MainPageView> getMainPageContent() {
        return articleService.getMainPageContent();
    }

    @GetMapping
    public List<Article> getArticles() {
        return articleService.getAll();
    }

    @isEmployee
    @PutMapping
    public Article createOrUpdateArticle(@Valid @RequestBody Article article, @RequestParam(required = false) Map<String, Object> params) {
        return articleService.addOrUpdate(article);
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable("id") int articleId) {
        return articleService.getById(articleId);
    }

    @isAdmin
    @DeleteMapping("/{id}")
    public boolean deleteArticleById(@PathVariable("id") int articleId) {
        return articleService.deleteById(articleId);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByArticle(@PathVariable("id") int articleId) {
        return articleService.getAllCommentsByArticleId(articleId);
    }

    @isLoggedIn
    @PutMapping("/{id}/comments")
    public Comment createOrUpdateComment(@PathVariable("id") int articleId, @RequestBody Comment comment) {
        return articleService.addOrUpdateComment(articleId, comment);
    }

    @GetMapping("/{id}/comments/{comment_id}")
    public Comment getCommentByArticleIdAndCommentId(@PathVariable("id") int articleId, @PathVariable("comment_id") int commentId) {
        return articleService.getCommentByArticleIdAndCommentId(articleId, commentId);
    }

    @isAdmin
    @DeleteMapping("/{id}/comments/{comment_id}")
    public boolean deleteCommentByArticleIdAndCommentId(@PathVariable("id") int articleId, @PathVariable("comment_id") int commentId) {
        return articleService.deleteCommentById(articleId, commentId);
    }
}