package com.example.databasedemo2.dataaccess.repositories.readonly;

import com.example.databasedemo2.dataaccess.views.ArticleInMakingView;

public interface ArticlesInMakingViewRepository extends BaseReadOnlyRepository<ArticleInMakingView, String> {
    ArticleInMakingView findByAuthorName(String name);
}
