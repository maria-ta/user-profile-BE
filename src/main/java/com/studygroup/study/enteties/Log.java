package com.studygroup.study.enteties;

import com.studygroup.study.enums.LogStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@DynamicUpdate
@Table(name="logs")
public class Log {
    @Id
    @Column(name="id_log")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name="status_key")
    public String key;

    @Column(name="user_id")
    public Long userId;

    @Column(name="entity_id")
    public Long entityId;

    @Column(name="log_datetime")
    public Timestamp datetime;

    public Log() {}

    public Log(Long userId, LogStatus status) {
        this.userId = userId;
        this.key = status.toString();
        this.datetime = new Timestamp(System.currentTimeMillis());
    }

    public Log(Long userId, Long entityId, LogStatus status) {
        this.userId = userId;
        this.entityId = entityId;
        this.key = status.toString();
        this.datetime = new Timestamp(System.currentTimeMillis());
    }
}
