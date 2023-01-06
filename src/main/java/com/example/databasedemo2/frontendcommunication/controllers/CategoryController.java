package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.services.CategoryService;
import com.example.databasedemo2.entitymanagement.entities.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getAll();
    }

    @PutMapping
    public Category createOrUpdateCategory(@RequestBody Category category) {
        return categoryService.addOrUpdate(category);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable("id") int categoryId) {
        return categoryService.getById(categoryId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteCategoryById(@PathVariable("id") int categoryId) {
        return categoryService.deleteById(categoryId);
    }
}