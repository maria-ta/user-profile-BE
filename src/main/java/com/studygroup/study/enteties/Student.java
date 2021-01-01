package com.studygroup.study.enteties;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Date;

@Entity
@DynamicUpdate
@Table(name="users")
public class Student {
    @Id
    @Column(name="id_user")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name="username", nullable = false, length = 256)
    public String username;
    @Column(name="email", nullable = false, length = 256)
    public String email;
    @Column(name="password", nullable = false, length = 256)
    public String password;

    @Column(name="firstname", nullable = true, length = 256)
    public String firstname;
    @Column(name="lastname", nullable = true, length = 256)
    public String lastname;

    @Column(name="gender", nullable = true, length = 256)
    public Character gender;

    @Column(name="birthdate", nullable = true)
    public Date birthdate;

    @Column(name="image", nullable = true, length = 1024)
    public String image;

    public Student() {}

    public Student(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Student(
        Long id,
        String username,
        String email,
        String password,
        String firstname,
        String lastname,
        Character gender,
        Date birthdate,
        String image
    ) {
        this.id = id;

        this.username = username;
        this.email = email;
        this.password = password;

        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.birthdate = birthdate;
        this.image = image;
    }

    public Student(
            String username,
            String email,
            String password,
            String firstname,
            String lastname,
            Character gender,
            Date birthdate,
            String image
    ) {
        this.username = username;
        this.email = email;
        this.password = password;

        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.birthdate = birthdate;
        this.image = image;
    }
}
