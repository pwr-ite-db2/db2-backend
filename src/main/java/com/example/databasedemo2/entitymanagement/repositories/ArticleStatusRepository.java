package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleStatusRepository extends JpaRepository<ArticleStatus, Integer> {
}
