package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.User;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends BaseRepository<User, Integer> {
    @Override
    default Set<User> findAllWithParam(String key, String val) {
        return key.equals("role") ? findAllByRole_Id(Integer.parseInt(val)) : Collections.emptySet();
    }

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Set<User> findAllByRole_Id(int roleId);
}
