package com.example.databasedemo2.dataaccess.entities;

import com.fasterxml.jackson.annotation.*;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Article {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn (name = "category_id", referencedColumnName = "id")
    private Category category;

    private String title;

    private String text;

    private boolean adultContent;

    @ManyToOne()
    @JoinColumn (name = "status_id", referencedColumnName = "id")
    private ArticleStatus articleStatus;

    @ManyToOne()
    @JoinColumn (name = "style_id", referencedColumnName = "id")
    private Style style;

    private int viewCount;

    private Date removedAt;

    @ManyToOne()
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private User author;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "article")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Change> changeHistory;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable (
            name = "tag_to_articles",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags;

    @OneToMany(mappedBy = "article")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Comment> comments;
}