package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseService<Category, Integer> {

    @Autowired
    public CategoryService(JpaRepository<Category, Integer> repository) {
        super(repository);
    }

    @Override
    protected void handle() {

    }
}
