package com.example.databasedemo2.entitymanagement.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

@Entity(name = "ArticleInEditView")
@Table(name = "articles_in_edit_view", schema = "public",  catalog = "projekt_db")
@Immutable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleInEditView {
    @Id
    private int articleId;

    private String title;

    private int userId;

    private String editor;
}