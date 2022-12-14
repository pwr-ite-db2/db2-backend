package com.example.databasedemo2.dataaccess.repositories;

import com.example.databasedemo2.dataaccess.entities.Change;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangesRepository extends JpaRepository<Change, Integer> {

}