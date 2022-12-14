package com.example.databasedemo2.dataaccess.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private Article article;

    private String subtitle;

    private String text;

    private int orderNum;

    public Chapter(int articleId, String subtitle, String text, int orderNum) {
        this.article = Article.builder().id(articleId).build();
        this.subtitle = subtitle;
        this.text = text;
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", article id=" + article.getId() +
                ", subtitle='" + subtitle + '\'' +
                ", text='" + text + '\'' +
                ", order=" + orderNum +
                '}';
    }
}
