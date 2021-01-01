package com.studygroup.study.enteties;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name="articles")
public class Article {

    @Id
    @Column(name="id_article")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name="title", nullable = false, length = 256)
    public String title;

    @Column(name="text", nullable = false)
    public String text;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id_user")
    public Student author;

    @Column(name="creation_datetime", nullable = false)
    public Date creationDate;

    @ManyToMany
    @JoinTable(
        name = "articles_tags",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    public Set<Tag> tags;

    public Article() {}

    public Article(String title, String text, Student author, Set<Tag> tags) {
        this.title = title;
        this.text = text;
        this.author = author;
        this.tags = tags;
        this.creationDate = new Date(System.currentTimeMillis());
    }
}
