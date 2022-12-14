package com.example.databasedemo2.dataaccess.repositories;

import com.example.databasedemo2.dataaccess.entities.Style;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StyleRepository extends JpaRepository<Style, Integer> {
}
