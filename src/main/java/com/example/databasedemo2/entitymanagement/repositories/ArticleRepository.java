package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.Article;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface ArticleRepository extends BaseRepository<Article, Integer> {

    @Override
    default Set<Article> findAllWithParam(String key, String val) {
        return switch (key) {
            case "category" ->  findAllByCategory_Id(Integer.parseInt(val));
            case "author" -> findAllByAuthor_Name(val);
            case "tags" -> findByTags_IdIn(createTagsFromString(val));
            default -> Collections.emptySet();
        };
    }

    Set<Article> findAllByCategory_Id(int categoryId);
    Set<Article> findAllByAuthor_Name(String name);
    Set<Article> findByTags_IdIn(Collection<Integer> tags_id);

    private Collection<Integer> createTagsFromString(String tags) {
        Set<Integer> returnSet = new HashSet<>();
        String [] tagsArr = tags.substring(tags.indexOf('=') + 1).split(",");

        for (String tagId : tagsArr)
            returnSet.add(Integer.parseInt(tagId));

        return returnSet;
    }
}