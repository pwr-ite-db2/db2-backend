package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.Change;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface ChangesRepository extends BaseRepository<Change, Integer> {
    @Override
    default Set<Change> findAllWithParam(String key, String val) {
        return switch (key) {
            case "user" -> findAllByUser_Id(Integer.parseInt(val));
            case "article" -> findAllByArticle_Id(Integer.parseInt(val));
            default -> Collections.emptySet();
        };
    }

    Set<Change> findAllByUser_Id(int userId);

    Set<Change> findAllByArticle_Id(int articleId);

    List<Change> getChangesByArticle_IdOrderByVersion(int articleId);
}