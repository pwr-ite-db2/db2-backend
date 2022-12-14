package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.dataaccess.entities.Chapter;
import com.example.databasedemo2.dataaccess.repositories.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChaptersController {
    private final ChapterRepository chapterRepository;

    @Autowired
    public ChaptersController(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @GetMapping("/chapters")
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }
}
