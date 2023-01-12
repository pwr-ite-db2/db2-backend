package com.example.databasedemo2.entitymanagement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity(name = "Comment")
@Table(name = "comments", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(message = "id cannot be less than 0!", value = 0)
    private int id;

    private Date createdAt;

    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "article_id", referencedColumnName = "id")
    @JsonIgnore
    private Article article;

    @ManyToOne
    @JoinColumn (name = "comment_id", referencedColumnName = "id")
    @ToString.Exclude
    @JsonBackReference
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private List<Comment> responses;

    // default false
    private boolean reported;

    @Transient
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private boolean read = false;

    @ManyToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private User user;

    @Size(max = 256, message = "Comment text must be less than 256 characters!")
    private String text;
}