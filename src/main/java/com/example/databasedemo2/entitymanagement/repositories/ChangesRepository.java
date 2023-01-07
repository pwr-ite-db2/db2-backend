package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.Change;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChangesRepository extends JpaRepository<Change, Integer> {
    List<Change> getChangesByArticle_IdOrderByVersion(int articleId);
}