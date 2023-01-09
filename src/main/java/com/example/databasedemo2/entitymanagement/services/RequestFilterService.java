package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.repositories.BaseRepository;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class RequestFilterService <T, ID> {
    private final BaseRepository<T, ID> repository;

    public List<T> findAll(Map<String, String> params) {
        if (params.isEmpty())
            return repository.findAll();

        Set<T> resultSet = new HashSet<>();
        boolean sum = Boolean.parseBoolean(params.getOrDefault("sum", "false"));    //defaults to false
        params.remove("sum");
        boolean first = true;

        for (Map.Entry<String, String> keyVal : params.entrySet()) {
            String key = keyVal.getKey();
            String val = keyVal.getValue();
            Set<T> partialSet = repository.findAllWithParam(key, val);
            if (Boolean.logicalOr(sum, first)){
                resultSet.addAll(partialSet);
                first = false;
            }
            else
                resultSet.retainAll(partialSet);
        }

        return new LinkedList<>(resultSet);
    }
}