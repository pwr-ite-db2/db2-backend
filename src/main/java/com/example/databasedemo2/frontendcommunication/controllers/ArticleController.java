package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.businesslogic.services.ArticleService;
import com.example.databasedemo2.dataaccess.entities.Article;
import com.example.databasedemo2.dataaccess.entities.Comment;
import com.example.databasedemo2.dataaccess.views.MainPageView;
import com.example.databasedemo2.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final JwtService jwtService;

    @GetMapping("/main-page")
    public List<MainPageView> getMainPageContent() {
        return articleService.getMainPageContent();
    }

    @GetMapping
    public List<Article> getArticles() {
        return articleService.getAll();
    }

    @PutMapping
    public Article createOrUpdateArticle(@RequestBody Article article) {
        return articleService.addOrUpdate(article);
    }

    @GetMapping("/{id}")
    public Article getArticleById(@RequestHeader Map<String, String> headers, @PathVariable("id") int articleId) {
        String jwtHeader = headers.get("authorization");
        String jwt = jwtService.getTokenFromHeader(jwtHeader);
        System.out.println(jwtService.getUserId(jwt));

        return articleService.getById(articleId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteArticleById(@PathVariable("id") int articleId) {
        return articleService.deleteById(articleId);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByArticle(@PathVariable("id") int articleId) {
        return articleService.getAllCommentsByArticleId(articleId);
    }

    @PutMapping("/{id}/comments")
    public Comment createOrUpdateComment(@PathVariable("id") int articleId, @RequestBody Comment comment) {
        return articleService.addOrUpdateComment(articleId, comment);
    }

    @GetMapping("/{id}/comments/{comment_id}")
    public Comment getCommentByArticleIdAndCommentId(@PathVariable("id") int articleId, @PathVariable("comment_id") int commentId) {
        return articleService.getCommentByArticleIdAndCommentId(articleId, commentId);
    }

    @DeleteMapping("/{id}/comments/{comment_id}")
    public boolean deleteCommentByArticleIdAndCommentId(@PathVariable("id") int articleId, @PathVariable("comment_id") int commentId) {
        return articleService.deleteCommentById(articleId, commentId);
    }
}