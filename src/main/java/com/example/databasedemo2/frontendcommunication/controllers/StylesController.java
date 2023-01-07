package com.example.databasedemo2.frontendcommunication.controllers;

import com.example.databasedemo2.entitymanagement.services.StylesService;
import com.example.databasedemo2.entitymanagement.entities.Style;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/styles")
@RequiredArgsConstructor
public class StylesController {
    private final StylesService stylesService;

    @GetMapping
    public List<Style> getStyles(@RequestParam(required = false) Map<String, Object> params) {
        return stylesService.getAll(params);
    }

    @PutMapping
    public Style createOrUpdateStyle(@RequestBody Style style, @RequestParam(required = false) Map<String, Object> params) {
        return stylesService.addOrUpdate(style, params);
    }

    @GetMapping("/{id}")
    public Style getStyleById(@PathVariable("id") int styleId) {
        return stylesService.getById(styleId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteStyleById(@PathVariable("id") int styleId) {
        return stylesService.deleteById(styleId);
    }
}
