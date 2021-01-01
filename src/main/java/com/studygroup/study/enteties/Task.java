package com.studygroup.study.enteties;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Date;

@Entity
@DynamicUpdate
@Table(name="tasks")
public class Task {
    @Id
    @Column(name="id_task")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name="title")
    public String title;

    @Column(name="description")
    public String description;

    @Column(name="deadline")
    @Basic(optional=true)
    public Date deadline;

    @Column(name="duration")
    @Basic(optional=true)
    public Long duration;

    @Column(name="course_id")
    public Long courseId;

    @Column(name="task_order")
    public Long order;

    public Task() {}

    public Task(String title, String description, Long duration, Long courseId, Long order) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.courseId = courseId;
        this.order = order;
    }
}
