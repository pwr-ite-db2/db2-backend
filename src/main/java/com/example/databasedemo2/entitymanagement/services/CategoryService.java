package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Category;
import com.example.databasedemo2.entitymanagement.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseService<Category, Integer> {

    @Autowired
    public CategoryService(CategoryRepository repository) {
        super(repository);
    }
}
