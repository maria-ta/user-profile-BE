package com.studygroup.study.enteties;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@DynamicUpdate
@Table(name="user_tasks")
public class Submission {
    @Id
    @Column(name="id_user_task")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    public Student student;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id_task")
    public Task task;

    @Column(name="datetime_started")
    public Timestamp startedDateTime;

    @Column(name="datetime_finished")
    public Timestamp finishedDateTime;

    @Column(name="submission")
    public String comment;

    @Column(name="mark")
    public Long mark;

    public BigDecimal finished;

    public BigDecimal planned;

    public Submission() {}

    public Submission(Task task, Student student) {
        this.student = student;
        this.task = task;
        this.startedDateTime = new Timestamp(System.currentTimeMillis());
    }

    public void addComment(String comment) {
        this.comment = comment;
    }

    public void finish(String comment) {
        this.comment = comment;
        this.finishedDateTime = new Timestamp(System.currentTimeMillis());
    }

    public boolean isFinished() {
        return finishedDateTime != null;
    }
}
