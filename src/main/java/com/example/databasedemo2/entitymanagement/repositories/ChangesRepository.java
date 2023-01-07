package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.Change;

import java.util.List;

public interface ChangesRepository extends BaseRepository<Change, Integer> {
    List<Change> getChangesByArticle_IdOrderByVersion(int articleId);
}