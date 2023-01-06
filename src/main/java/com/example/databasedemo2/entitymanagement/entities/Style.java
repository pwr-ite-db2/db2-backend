package com.example.databasedemo2.entitymanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Min(message = "id cannot be less than 0!", value = 0)
    private int id;

    @NotNull(message = "Layout cannot be empty!")
    @Size(max = 256, message = "Layout text must be less than 256 characters!")
    private String layout;

    @Min(message = "Text size must be greater than 0!", value = 1)
    private short textSize;

    @NotNull(message = "Role name cannot be empty!")
    @Size(max = 256, message = "ImpTextHtmlStyle text must be less than 256 characters!")
    private String impTextHtmlStyle;

    @OneToMany(mappedBy = "style")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Article> articlesWithStyle;
}
