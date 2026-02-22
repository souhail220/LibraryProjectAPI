package com.library.libraryprojectapi.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String description;
    private Date createdAt;

    @ManyToOne
    @ToString.Exclude
    private User author;
    
    @ManyToMany
    @ToString.Exclude
    private List<Categorie> categories;

}
