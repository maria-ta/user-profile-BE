package com.studygroup.study.enteties;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name="courses")
public class Course {
    @Id
    @Column(name="id_course")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name="title")
    public String title;

    @Column(name="description")
    public String description;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id_user")
    public Student author;

    public Course() {};

    public Course(Student author, String title, String description) {
        this.author = author;
        this.title = title;
        this.description = description;
    }

//    @OneToMany(
//            targetEntity = Task.class,
//            cascade = CascadeType.ALL,
//            fetch = FetchType.EAGER,
//            orphanRemoval = true
//    ) //non-owning side
//    public List<Task> tasks = new ArrayList<>();
}
