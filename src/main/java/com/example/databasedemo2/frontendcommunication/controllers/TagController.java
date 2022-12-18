package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.businesslogic.services.TagService;
import com.example.databasedemo2.dataaccess.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getTags() {
        return tagService.getAll();
    }

    @PutMapping
    public Tag createOrUpdateTag(@RequestBody Tag tag) {
        return tagService.addOrUpdate(tag);
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable("id") int tagId) {
        return tagService.getById(tagId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteTagById(@PathVariable("id") int tagId) {
        return tagService.deleteById(tagId);
    }
}