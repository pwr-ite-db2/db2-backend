package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Chapter;
import com.example.databasedemo2.entitymanagement.repositories.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterService extends BaseService <Chapter, Integer> {
    @Autowired
    public ChapterService(ChapterRepository repository) {
        super(repository);
    }
}
