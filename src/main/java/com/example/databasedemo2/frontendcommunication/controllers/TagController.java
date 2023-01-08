package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.entities.Tag;
import com.example.databasedemo2.entitymanagement.services.TagService;
import com.example.databasedemo2.security.anotations.isAdmin;
import com.example.databasedemo2.security.anotations.isEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public List<Tag> getTags(@RequestParam(required = false) Map<String, String> params) {
        return tagService.getAll(params);
    }

    @isEmployee
    @PutMapping
    public Tag createOrUpdateTag(@RequestBody Tag tag, @RequestParam(required = false) Map<String, String> params) {
        return tagService.addOrUpdate(tag, params);
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable("id") int tagId) {
        return tagService.getById(tagId);
    }

    @isAdmin
    @DeleteMapping("/{id}")
    public boolean deleteTagById(@PathVariable("id") int tagId) {
        return tagService.deleteById(tagId);
    }
}