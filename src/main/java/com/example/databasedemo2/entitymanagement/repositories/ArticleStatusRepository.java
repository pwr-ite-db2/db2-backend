package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleStatusRepository extends JpaRepository<ArticleStatus, Integer> {
    Optional<ArticleStatus> findByName(String name);
}
