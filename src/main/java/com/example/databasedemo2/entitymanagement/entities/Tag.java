package com.example.databasedemo2.entitymanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Min(message = "id cannot be less than 0!", value = 0)
    private int id;

    @NotNull(message = "Tag name cannot be empty!")
    @Size(max = 100, message = "Tag text must be less than 100 characters!")
    private String name;

    @ManyToMany (mappedBy = "tags", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Article> taggedArticles;
}