package com.example.databasedemo2.entitymanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collections;
import java.util.Set;

@NoRepositoryBean
public interface BaseRepository <T, ID> extends JpaRepository <T, ID> {
    default Set<T> findAllWithParam(String key, String val) {
        return Collections.emptySet();
    }
}