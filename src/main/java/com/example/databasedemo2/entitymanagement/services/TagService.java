package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Tag;
import com.example.databasedemo2.entitymanagement.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService extends BaseService<Tag, Integer> {

    @Autowired
    public TagService(BaseRepository<Tag, Integer> repository) {
        super(repository);
    }
}
