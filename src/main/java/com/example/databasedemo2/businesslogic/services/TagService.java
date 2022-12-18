package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.entities.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TagService extends BaseService<Tag, Integer> {
    public TagService(JpaRepository<Tag, Integer> repository) {
        super(repository);
    }

    @Override
    protected void handle() {

    }
}
