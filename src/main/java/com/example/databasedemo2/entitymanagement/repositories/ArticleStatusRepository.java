package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.ArticleStatus;

import java.util.Optional;

public interface ArticleStatusRepository extends BaseRepository<ArticleStatus, Integer> {
    Optional<ArticleStatus> findByName(String name);
}