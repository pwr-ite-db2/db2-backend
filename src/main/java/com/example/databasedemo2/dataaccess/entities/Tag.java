package com.example.databasedemo2.dataaccess.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "Tag")
@Table(name = "tags", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany (mappedBy = "tags")
    @JsonIgnore
//    @JsonBackReference("article-tag")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Article> taggedArticles;
}
