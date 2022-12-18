package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.Article;
import com.example.databasedemo2.dataaccess.repositories.readonly.ArticlesInEditViewRepository;
import com.example.databasedemo2.dataaccess.repositories.readonly.ArticlesInMakingViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticleService extends BaseService<Article, Integer> {
    private final ArticlesInEditViewRepository inEditViewRepository;
    private final ArticlesInMakingViewRepository inMakingViewRepository;

    @Autowired
    public ArticleService(JpaRepository<Article, Integer> repository, ArticlesInEditViewRepository inEditViewRepository, ArticlesInMakingViewRepository inMakingViewRepository) {
        super(repository);
        this.inEditViewRepository = inEditViewRepository;
        this.inMakingViewRepository = inMakingViewRepository;
    }
    @Override
    protected void handle() {

    }
}