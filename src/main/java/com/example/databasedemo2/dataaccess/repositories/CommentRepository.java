package com.example.databasedemo2.dataaccess.repositories;

import com.example.databasedemo2.dataaccess.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
