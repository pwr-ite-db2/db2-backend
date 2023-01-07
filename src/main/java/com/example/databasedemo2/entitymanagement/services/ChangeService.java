package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Change;
import com.example.databasedemo2.entitymanagement.repositories.ChangesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeService extends BaseService<Change, Integer> {
    public ChangeService(JpaRepository<Change, Integer> repository) {
        super(repository);
    }

    public short getLatestVersionNumberByArticleId(int articleId) {
        if (articleId == 0)
            return 1;

        List<Change> changesByArticleId = ((ChangesRepository) repository).getChangesByArticle_IdOrderByVersion(articleId);

        if (changesByArticleId != null && changesByArticleId.size() != 0) {
            return changesByArticleId.get(changesByArticleId.size() - 1).getVersion();
        }

        return 1;
    }
}
