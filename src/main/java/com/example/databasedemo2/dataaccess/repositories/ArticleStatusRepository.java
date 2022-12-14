package com.example.databasedemo2.dataaccess.repositories;

import com.example.databasedemo2.dataaccess.entities.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleStatusRepository extends JpaRepository<ArticleStatus, Integer> {
}
