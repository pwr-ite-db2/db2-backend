package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.entities.ArticleStatus;
import com.example.databasedemo2.entitymanagement.services.ArticleStatusService;
import com.example.databasedemo2.security.anotations.isAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
public class ArticleStatusController {
    private final ArticleStatusService articleStatusService;

    @GetMapping
    public List<ArticleStatus> getStatuses(@RequestParam(required = false) Map<String, String> params) {
        return articleStatusService.getAll(params);
    }

    @isAdmin
    @PutMapping
    public ArticleStatus createOrUpdateStatus(@RequestBody ArticleStatus articleStatus, @RequestParam(required = false) Map<String, String> params) {
        return articleStatusService.addOrUpdate(articleStatus, params);
    }

    @GetMapping("/{id}")
    public ArticleStatus getStatusById(@PathVariable("id") int statusId) {
        return articleStatusService.getById(statusId);
    }

    @isAdmin
    @DeleteMapping("/{id}")
    public boolean deleteStatusById(@PathVariable("id") int statusId) {
        return articleStatusService.deleteById(statusId);
    }
}
