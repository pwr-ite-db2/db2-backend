package com.example.databasedemo2.entitymanagement.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import java.util.Date;

@Entity(name = "MainPageView")
@Table(name = "main_page_view", schema = "public",  catalog = "projekt_db")
@Immutable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MainPageView {
    @Id
    private int articleId;

    private String title;

    private int viewCount;

    private String categoryName;

    private String author;

    @Column(name = "date")
    private Date releaseDate;
}
