package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Style;
import com.example.databasedemo2.entitymanagement.repositories.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class StylesService extends BaseService<Style, Integer> {
    public StylesService(BaseRepository<Style, Integer> repository) {
        super(repository);
    }
}
