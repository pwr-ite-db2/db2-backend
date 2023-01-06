package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.services.TagService;
import com.example.databasedemo2.entitymanagement.entities.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

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