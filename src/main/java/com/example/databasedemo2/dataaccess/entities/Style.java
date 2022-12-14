package com.example.databasedemo2.dataaccess.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Style")
@Table(name = "styles", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Style {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    private String layout;

    private short textSize;

    private String impTextHtmlStyle;

    @OneToMany(mappedBy = "style")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Article> articlesWithStyle;
}
