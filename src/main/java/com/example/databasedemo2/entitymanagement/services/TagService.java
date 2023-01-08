package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Tag;
import com.example.databasedemo2.entitymanagement.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService extends BaseService<Tag, Integer> {

    @Autowired
    public TagService(TagRepository repository) {
        super(repository);
    }
}
