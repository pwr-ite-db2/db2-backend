package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ChapterService extends BaseService <Chapter, Integer> {

    @Autowired
    public ChapterService(JpaRepository<Chapter, Integer> repository) {
        super(repository);
    }
}
