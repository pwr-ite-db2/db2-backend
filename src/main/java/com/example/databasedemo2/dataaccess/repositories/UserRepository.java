package com.example.databasedemo2.dataaccess.repositories;

import com.example.databasedemo2.dataaccess.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
