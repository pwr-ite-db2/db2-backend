package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.ArticleStatus;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public interface ArticleStatusRepository extends BaseRepository<ArticleStatus, Integer> {
    @Override
    default Set<ArticleStatus> findAllWithParam(String key, String val) {
        return key.equals("name") ?  findAllByName(val) : Collections.emptySet();
    }
    Set<ArticleStatus> findAllByName(String name);
    Optional<ArticleStatus> findByName(String name);
}