package com.example.databasedemo2.entitymanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity(name = "Category")
@Table(name = "categories", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(message = "id cannot be less than 0!", value = 0)
    private int id;

    @NotNull(message = "Category name cannot be empty!")
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Article> articlesWithCategory;
}
