package com.example.databasedemo2.businesslogic.services;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public abstract class BaseService <T, ID> {
    protected final JpaRepository<T, ID> repository;

    public BaseService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T addOrUpdate(T entity) {
        return repository.save(entity);
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public T getById(ID id) {
        return repository.findById(id).get();
    }

    public boolean deleteById(ID id) {
        repository.deleteById(id);
        return true;
    }

    // error handling (TODO)
    protected abstract void handle();
}
