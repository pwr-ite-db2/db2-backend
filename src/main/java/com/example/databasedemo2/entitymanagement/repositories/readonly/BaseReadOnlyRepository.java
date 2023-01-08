package com.example.databasedemo2.entitymanagement.repositories.readonly;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean
public interface BaseReadOnlyRepository<T, ID> extends Repository<T, ID> {

    List<T> findAll();
}