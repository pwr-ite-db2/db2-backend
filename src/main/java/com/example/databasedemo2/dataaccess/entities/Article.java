package com.example.databasedemo2.dataaccess.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity(name = "Article")
@Table(name = "articles", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn (name = "category_id", referencedColumnName = "id")
    @JsonManagedReference
    private Category category;

    private String title;

    private String text;

    private boolean adultContent;

    @ManyToOne()
    @JoinColumn (name = "status_id", referencedColumnName = "id")
    @JsonManagedReference
    private ArticleStatus articleStatus;

    @ManyToOne()
    @JoinColumn (name = "style_id", referencedColumnName = "id")
    @JsonManagedReference
    private Style style;

    private int viewCount;

    private Date removedAt;

    @ManyToOne()
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    @JsonManagedReference
    private User author;

    /**
     * Custom constructors for adding new article entities, that creates new mandatory class fields.
     */
    public Article(int categoryId, String title, String text, boolean adultContent, int articleStatusId, int styleId, int viewCount, Date removedAt, int userId) {
        this.category = Category.builder().id(categoryId).build();
        this.title = title;
        this.text = text;
        this.adultContent = adultContent;
        this.articleStatus = ArticleStatus.builder().id(articleStatusId).build();
        this.style = Style.builder().id(styleId).build();
        this.viewCount = viewCount;
        this.removedAt = removedAt;
        this.author = User.builder().id(userId).build();
    }

    public Article(int categoryId, String title, String text, boolean adultContent, int styleId, Date removedAt, int userId) {
        this(categoryId, title, text, adultContent, 1, styleId, 0, removedAt, userId);
    }

    @OneToMany(mappedBy = "article")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "article")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Change> changeHistory;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable (
            name = "tag_to_articles",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonManagedReference
    private Set<Tag> tags;

    @OneToMany(mappedBy = "article")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Comment> comments;

    public boolean addNewTag(Tag tag) {
        return this.getTags().add(tag);
    }
}