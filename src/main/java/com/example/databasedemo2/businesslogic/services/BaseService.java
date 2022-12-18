package com.example.databasedemo2.businesslogic.services;

import com.example.databasedemo2.dataaccess.repositories.BaseRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
public abstract class BaseService <T, ID> {
    private final BaseRepository<T, ID> repository;

    public BaseService(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T addOrUpdate(T entity) {
        return repository.save(entity);
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    public boolean deleteById(ID id) {
        repository.deleteById(id);
        return true;
    }
}
