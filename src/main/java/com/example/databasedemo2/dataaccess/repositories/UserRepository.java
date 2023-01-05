package com.example.databasedemo2.dataaccess.repositories;

import com.example.databasedemo2.dataaccess.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}