package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Style;
import com.example.databasedemo2.entitymanagement.repositories.StyleRepository;
import org.springframework.stereotype.Service;

@Service
public class StylesService extends BaseService<Style, Integer> {
    public StylesService(StyleRepository repository) {
        super(repository);
    }
}
