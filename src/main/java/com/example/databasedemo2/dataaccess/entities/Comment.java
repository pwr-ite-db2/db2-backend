package com.example.databasedemo2.dataaccess.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "Comment")
@Table(name = "comments", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date created_at;

    private Date updated_at;

    @ManyToOne()
    @JoinColumn (name = "article_id", referencedColumnName = "id")
    @JsonManagedReference
    private Article article;

    @OneToOne()
    @JoinColumn (name = "comment_id", referencedColumnName = "id")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Comment responseTo;

    @ManyToOne()
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    @JsonManagedReference
    private User user;

    private String text;

    public Comment(Date created_at, Date updated_at, int articleId, int commentId, int userId, String text) {
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.article = Article.builder().id(articleId).build();
        this.responseTo = Comment.builder().id(commentId).build();
        this.user = User.builder().id(userId).build();
        this.text = text;
    }
}
