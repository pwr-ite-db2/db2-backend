package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Change;
import com.example.databasedemo2.entitymanagement.repositories.BaseRepository;
import com.example.databasedemo2.entitymanagement.repositories.ChangesRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ChangeService extends BaseService<Change, Integer> {
    public ChangeService(BaseRepository<Change, Integer> repository) {
        super(repository);
    }

    // Disable querying all changes
    @Override
    public List<Change> getAll(Map<String, String> params) {
        if(params.isEmpty())
            return Collections.emptyList();

        return super.getAll(params);
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
