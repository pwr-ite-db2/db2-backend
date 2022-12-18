package com.example.databasedemo2.dataaccess.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Chapter")
@Table(name = "chapters", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn (name = "article_id", referencedColumnName = "id")
    private Article article;

    private String subtitle;

    private String text;

    private int orderNum;
}
