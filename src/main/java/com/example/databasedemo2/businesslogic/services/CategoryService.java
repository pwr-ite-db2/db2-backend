package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService extends BaseService<Category, Integer> {
    public CategoryService(JpaRepository<Category, Integer> repository) {
        super(repository);
    }

    @Override
    protected void handle() {

    }
}
