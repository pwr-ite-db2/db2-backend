package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Change;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ChangeService extends BaseService<Change, Integer> {
    public ChangeService(JpaRepository<Change, Integer> repository) {
        super(repository);
    }
}
