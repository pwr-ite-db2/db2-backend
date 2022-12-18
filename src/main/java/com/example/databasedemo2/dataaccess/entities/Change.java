package com.example.databasedemo2.dataaccess.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "Change")
@Table(name = "changes_history", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Change {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private short version;

    private String notes;

    private Date date;

    @ManyToOne
    @JoinColumn (name = "article_id", referencedColumnName = "id")
    private Article article;

    @ManyToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn (name = "status_id", referencedColumnName = "id")
    private ArticleStatus statusAfterChanges;
}