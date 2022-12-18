package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.Category;
import com.example.databasedemo2.dataaccess.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addOrUpdateCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public boolean deleteCategoryById(int id) {
        categoryRepository.deleteById(id);
        return true;
    }
}
