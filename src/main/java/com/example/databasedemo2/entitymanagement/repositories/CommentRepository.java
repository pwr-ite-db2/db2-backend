package com.example.databasedemo2.entitymanagement.repositories;

import com.example.databasedemo2.entitymanagement.entities.Comment;

import java.util.List;

public interface CommentRepository extends BaseRepository <Comment, Integer> {
    List<Comment> findAllByReported(boolean reported);
}