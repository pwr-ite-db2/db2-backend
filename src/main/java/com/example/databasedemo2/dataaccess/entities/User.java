package com.example.databasedemo2.dataaccess.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity(name = "User")
@Table(name = "users", schema = "public",  catalog = "projekt_db")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date createdAt;

    private Date updatedAt;

    @ManyToOne
    @JoinColumn (name = "role_id", referencedColumnName = "id")
    @JsonManagedReference
    private Role role;

    private String email;

    private String name;

    private char[] password;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Change> changesByUser;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Comment> commentsByUser;

    @OneToMany(mappedBy = "author")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Article> articlesByUser;
}