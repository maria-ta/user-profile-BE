package com.studygroup.study.enteties;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name="tags")
public class Tag {
    @Id
    @Column(name="id_tag")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name="label")
    public String label;

    public Long count;

    public Tag() {}

    public Tag(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public Tag(Long id, String label, Long count) {
        this.id = id;
        this.label = label;
        this.count = count;
    }
}
