package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.entities.Article;
import com.example.databasedemo2.entitymanagement.entities.Change;
import com.example.databasedemo2.entitymanagement.entities.Comment;
import com.example.databasedemo2.entitymanagement.services.ArticleService;
import com.example.databasedemo2.entitymanagement.views.MainPageView;
import com.example.databasedemo2.security.anotations.*;
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
    public List<MainPageView> getMainPageContent(@RequestParam(defaultValue = "7") String days) {
        return articleService.getMainPageContent(days);
    }

    @GetMapping
    public List<Article> getArticles(@RequestParam(required = false) Map<String, String> params) {
        return articleService.getAll(params);
    }

    @isEmployee
    @PutMapping
    public Article createOrUpdateArticle(@Valid @RequestBody Article article, @RequestParam(required = false) Map<String, String> params) {
        return articleService.addOrUpdate(article, params);
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
    public Comment createOrUpdateComment(@PathVariable("id") int articleId, @Valid @RequestBody Comment comment) {
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

    @isAuthor
    @PutMapping("/submit")
    public Article submitArticleForEditing(@Valid @RequestBody Article article) {
        return articleService.submitArticleForEditing(article);
    }

    @isEditor
    @PutMapping("/publish")
    public Article publishArticle(@Valid @RequestBody Article article) {
        return articleService.publishArticle(article);
    }

    @isEditor
    @GetMapping("/{id}/edit")
    public Article pickArticleForEditing(@PathVariable("id") int articleId) {
        return articleService.pickArticleForEditing(articleId);
    }

    @isEditor
    @GetMapping("/{id}/rollback")
    public Article rollbackArticle(@PathVariable("id") int articleId) {
        return articleService.rollbackArticle(articleId);
    }

    @isAdmin
    @GetMapping("/changes")
    public List<Change> getChanges (@RequestParam Map<String, String> params) {
        return articleService.getChanges(params);
    }
}