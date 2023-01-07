package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.exceptions.custom.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

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

    public T addOrUpdate(T entity, Map<String, Object> params) throws EntityNotFoundException {
        return repository.save(entity);
    }

    public List<T> getAll(Map<String, Object> params) {
        return repository.findAll();
    }

    public T getById(ID id) throws ResourceNotFoundException {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public boolean deleteById(ID id) {
        repository.deleteById(id);
        return true;
    }
}
