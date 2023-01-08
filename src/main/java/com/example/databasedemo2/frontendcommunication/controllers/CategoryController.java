package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.entities.Category;
import com.example.databasedemo2.entitymanagement.services.CategoryService;
import com.example.databasedemo2.security.anotations.isAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories(@RequestParam(required = false) Map<String, String> params) {
        return categoryService.getAll(params);
    }

    @isAdmin
    @PutMapping
    public Category createOrUpdateCategory(@RequestBody Category category, @RequestParam(required = false) Map<String, String> params) {
        return categoryService.addOrUpdate(category, params);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable("id") int categoryId) {
        return categoryService.getById(categoryId);
    }

    @isAdmin
    @DeleteMapping("/{id}")
    public boolean deleteCategoryById(@PathVariable("id") int categoryId) {
        return categoryService.deleteById(categoryId);
    }
}