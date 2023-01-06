package com.example.databasedemo2.entitymanagement.repositories.readonly;

import com.example.databasedemo2.entitymanagement.views.ArticleInMakingView;

import java.util.List;

public interface ArticlesInMakingViewRepository extends BaseReadOnlyRepository<ArticleInMakingView, Integer> {
    List<ArticleInMakingView> findAllByUserId(int userId);
}
