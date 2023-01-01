package com.example.databasedemo2.dataaccess.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

@Entity(name = "ArticleWaitingForEditView")
@Table(name = "articles_waiting_for_edit_view", schema = "public",  catalog = "projekt_db")
@Immutable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleWaitingForEditView {
    @Id
    private int articleId;

    private String title;

    private int userId;

    private String author;
}