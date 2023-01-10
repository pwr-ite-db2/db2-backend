package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.ArticleStatus;
import com.example.databasedemo2.entitymanagement.repositories.ArticleStatusRepository;
import com.example.databasedemo2.exceptions.custom.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ArticleStatusService extends BaseService<ArticleStatus, Integer> {
    private static final String DEFAULT_ARTICLE_STATUS = "UTWORZONY";

    public ArticleStatusService(ArticleStatusRepository repository) {
        super(repository);
    }

    ArticleStatus findByName(String statusName) throws ResourceNotFoundException {
        return ((ArticleStatusRepository) repository).findByName(statusName).orElseThrow(ResourceNotFoundException::new);
    }

    ArticleStatus getDefaultStatus() throws ResourceNotFoundException {
        return findByName(DEFAULT_ARTICLE_STATUS);
    }
}