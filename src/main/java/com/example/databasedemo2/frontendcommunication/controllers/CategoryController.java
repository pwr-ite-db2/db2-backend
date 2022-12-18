package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.businesslogic.services.CategoryService;
import com.example.databasedemo2.dataaccess.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping
    public Category createOrUpdateCategory(@RequestBody Category category) {
        return categoryService.addOrUpdateCategory(category);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable("id") int categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteCategoryById(@PathVariable("id") int categoryId) {
        return categoryService.deleteCategoryById(categoryId);
    }
}