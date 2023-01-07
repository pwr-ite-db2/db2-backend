package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.Article;
import com.example.databasedemo2.entitymanagement.entities.Tag;

import java.util.*;

public interface ArticleRepository extends BaseRepository<Article, Integer> {

    @Override
    default Set<Article> findAllWithParam(String key, String val) {
        return switch (key) {
            case "category" ->  findAllByCategory_Id(Integer.parseInt(val));
            case "author" -> findAllByAuthor_Name(val);
            case "tags" -> findAllByTagsIn(createTagsFromString(val));
            default -> Collections.emptySet();
        };
    }

    Set<Article> findAllByCategory_Id(int categoryId);
    Set<Article> findAllByAuthor_Name(String name);
    Set<Article> findAllByTagsIn(Collection<Set<Tag>> tags);

    private Collection<Set<Tag>> createTagsFromString(String tags) {
        Set<Tag> returnSet = new HashSet<>();
        String [] tagsArr = tags.substring(tags.indexOf('{'), tags.indexOf('}')).split(",");

        System.out.println(Arrays.toString(tagsArr));

        for (String tagId : tagsArr)
            returnSet.add(Tag.builder().id(Integer.parseInt(tagId)).build());

        return List.of(returnSet);
    }
}