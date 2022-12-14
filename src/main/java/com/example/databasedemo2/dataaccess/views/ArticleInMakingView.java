package com.example.databasedemo2.dataaccess.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

@Entity(name = "ArticleInMakingView")
@Table(name = "articles_in_making", schema = "public",  catalog = "projekt_db")
@Immutable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleInMakingView {
    @Id
    private String title;

    @Column(name = "name")
    private String authorName;
}
