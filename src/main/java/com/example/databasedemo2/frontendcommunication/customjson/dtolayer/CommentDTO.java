package com.example.databasedemo2.frontendcommunication.customjson.dtolayer;

import com.example.databasedemo2.configuration.ApplicationConfig;
import com.example.databasedemo2.entitymanagement.entities.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private int id;

    private Date createdAt;

    private Date updatedAt;

    private int articleId;

    private int userId;

    private String email;

    private String text;

    private String deleteCommentHref;

    public static CommentDTO buildFromComment(Comment comment) {
        return CommentDTO
                .builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .articleId(comment.getArticle().getId())
                .userId(comment.getUser().getId())
                .email(comment.getUser().getEmail())
                .text(comment.getText())
                .deleteCommentHref(ApplicationConfig.HOST_BASE_URL + "/articles/" + comment.getArticle().getId() +"/comments/" + comment.getId())
                .build();
    }
}
