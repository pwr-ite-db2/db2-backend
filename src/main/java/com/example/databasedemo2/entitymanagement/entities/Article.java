package com.example.databasedemo2.entitymanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity(name = "Article")
@Table(name = "articles", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Article {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Min(message = "id cannot be less than 0!", value = 0)
    private int id;

    @ManyToOne
    @JoinColumn (name = "category_id", referencedColumnName = "id")
    @NotNull(message = "Category cannot be empty!")
    private Category category;

    @NotNull(message = "Title cannot be empty!")
    @Size(max = 50, message = "Article title must be less than 50 characters!")
    private String title;

    @Size(max = 10000, message = "Article text must be less than 10000 characters!")
    private String text;

    private boolean adultContent;

    @ManyToOne
    @JoinColumn (name = "status_id", referencedColumnName = "id")
    private ArticleStatus articleStatus;

    @ManyToOne
    @JoinColumn (name = "style_id", referencedColumnName = "id")
    @NotNull(message = "Style cannot be empty!")
    private Style style;

    @Min(message = "View count cannot be less than 0!", value = 0)
    private int viewCount;

    private Date removedAt;

    @ManyToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private User author;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Change> changeHistory;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable (
            name = "tag_to_articles",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Comment> comments;
}