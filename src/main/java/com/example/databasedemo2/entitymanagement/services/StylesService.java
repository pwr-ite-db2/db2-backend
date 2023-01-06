package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class StylesService extends BaseService<Style, Integer> {
    public StylesService(JpaRepository<Style, Integer> repository) {
        super(repository);
    }
}
