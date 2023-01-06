package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService extends BaseService<Tag, Integer> {

    @Autowired
    public TagService(JpaRepository<Tag, Integer> repository) {
        super(repository);
    }
}
