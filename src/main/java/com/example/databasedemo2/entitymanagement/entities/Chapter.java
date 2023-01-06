package com.example.databasedemo2.entitymanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "Chapter")
@Table(name = "chapters", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Min(message = "id cannot be less than 0!", value = 0)
    private int id;

    @ManyToOne()
    @JoinColumn (name = "article_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private Article article;

    @Size(max = 50, message = "Chapter title must be less than 50 characters!")
    private String subtitle;

    @Size(max = 10000, message = "Chapter text must be less than 10000 characters!")
    private String text;

    @Min(message = "Order number cannot be less than 0!", value = 0)
    private int orderNum;
}