package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Chapter;
import com.example.databasedemo2.entitymanagement.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterService extends BaseService <Chapter, Integer> {
    @Autowired
    public ChapterService(BaseRepository<Chapter, Integer> repository) {
        super(repository);
    }
}
