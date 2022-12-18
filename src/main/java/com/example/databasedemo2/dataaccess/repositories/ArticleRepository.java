package com.example.databasedemo2.dataaccess.repositories;

import com.example.databasedemo2.dataaccess.entities.Article;
import com.example.databasedemo2.dataaccess.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findAllByCategory_Name(String name);
    List<Article> findAllByAuthor_Name(String name);
    List<Article> findAllByTagsIn(Collection<Set<Tag>> tags);
}