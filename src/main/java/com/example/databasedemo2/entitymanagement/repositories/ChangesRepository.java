package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.Change;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangesRepository extends JpaRepository<Change, Integer> {

}