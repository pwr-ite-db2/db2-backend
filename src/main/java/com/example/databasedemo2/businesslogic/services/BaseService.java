package com.example.databasedemo2.businesslogic.services;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
/**
 * Generic base service class providing simple CRUD functionality.
 * @param <T> the entity type
 * @param <ID> the entity ID type
 */
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
        return repository.findById(id).orElseThrow();
    }

    public boolean deleteById(ID id) {
        repository.deleteById(id);
        return true;
    }

    // error handling (TODO)
    @ExceptionHandler(IllegalArgumentException.class)
    protected abstract void handle();
}
