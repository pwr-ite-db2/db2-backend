package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.Comment;
import jakarta.validation.constraints.Email;

import java.util.Collections;
import java.util.Set;

public interface CommentRepository extends BaseRepository<Comment, Integer> {
    @Override
    default Set<Comment> findAllWithParam(String key, String val) {
        return key.equals("user") ?  findAllByUser_Email(val) : Collections.emptySet();
    }

    Set<Comment> findAllByUser_Email(@Email(message = "Not valid email address!") String user_email);
}
