package com.example.databasedemo2.dataaccess.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Article article;

    @OneToOne()
    @JoinColumn (name = "comment_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Comment responseTo;

    @ManyToOne()
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private User user;

    private String text;
}
