package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.repositories.BaseRepository;
import com.example.databasedemo2.exceptions.custom.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Generic base service class providing simple CRUD functionality.
 * @param <T> the entity type
 * @param <ID> the entity ID type
 */
@Transactional
public abstract class BaseService <T, ID> {
    protected final BaseRepository<T, ID> repository;
    protected final RequestFilterService<T, ID> filter;

    public BaseService(BaseRepository<T, ID> repository) {
        this.repository = repository;
        this.filter = new RequestFilterService<>(this.repository);
    }

    public T addOrUpdate(T entity, Map<String, String> params) throws EntityNotFoundException {
        return repository.save(entity);
    }

    public List<T> getAll(Map<String, String> params) {
        return filter.findAll(params);
    }

    public T getById(ID id) throws ResourceNotFoundException {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public boolean deleteById(ID id) {
        repository.deleteById(id);
        return true;
    }
}