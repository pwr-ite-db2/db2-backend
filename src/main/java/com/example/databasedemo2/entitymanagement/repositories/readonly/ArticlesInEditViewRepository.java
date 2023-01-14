package com.example.databasedemo2.entitymanagement.repositories.readonly;

import com.example.databasedemo2.entitymanagement.views.ArticleInEditView;

import java.util.List;
import java.util.Optional;

public interface ArticlesInEditViewRepository extends BaseReadOnlyRepository<ArticleInEditView, Integer> {
    List<ArticleInEditView> findAllByUserId(int userId);

    Optional<ArticleInEditView> findByArticleIdAndUserId(int articleId, int userId);
}