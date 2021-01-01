package com.studygroup.study.repos;

import com.studygroup.study.enteties.Log;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public interface LogRepo extends CrudRepository<Log, Long> {
    @Query(
        value = "SELECT tags.id_tag, tags.label, COUNT(tags.id_tag) AS count_tag FROM tags, articles_tags, logs WHERE tags.id_tag = articles_tags.tag_id AND logs.status_key = 'READ_ARTICLE' AND articles_tags.article_id = logs.entity_id AND logs.user_id = :userId GROUP BY tags.id_tag ORDER BY COUNT(tags.id_tag)",
        nativeQuery = true
    )
    List<Object[]> findDiscoveredTags(
        @Param("userId") Long userId
    );

    @Query(
            value = "SELECT tags.id_tag, tags.label, COUNT(tags.id_tag) AS count_tag FROM tags, articles_tags, logs WHERE tags.id_tag = articles_tags.tag_id AND logs.status_key = 'READ_ARTICLE' AND articles_tags.article_id = logs.entity_id AND logs.user_id = :userId GROUP BY tags.id_tag ORDER BY COUNT(tags.id_tag) DESC LIMIT :limit",
            nativeQuery = true
    )
    List<Object[]> findFavouriteTags(
            @Param("userId") Long userId,
            @Param("limit") int limit
    );

    @Query(
            value = "SELECT users.* FROM users, logs, articles WHERE logs.user_id = :userId AND logs.status_key = 'READ_ARTICLE' AND logs.entity_id = articles.id_article AND users.id_user = articles.author_id GROUP BY articles.author_id ORDER BY COUNT(articles.author_id) LIMIT :limit",
            nativeQuery = true
    )
    List<Object[]> findFavouriteAuthors(
            @Param("userId") Long userId,
            @Param("limit") int limit
    );

    @Query(
            value = "SELECT DATE(logs.log_datetime) AS date, count(logs.id_log) as count FROM logs WHERE logs.user_id = :userId AND DATE(logs.log_datetime) >= :start AND DATE(logs.log_datetime) <= :end GROUP BY DATE(logs.log_datetime)",
            nativeQuery = true
    )
    List<Object> findActivityTiming(
            @Param("userId") Long userId,
            @Param("start") Date start,
            @Param("end") Date end
    );

    @Query(
            value = "SELECT count(logs.id_log) as count FROM logs WHERE logs.user_id = :userId AND DATE(logs.log_datetime) = :date GROUP BY DATE(logs.log_datetime)",
            nativeQuery = true
    )
    BigDecimal findActivityForDay(
            @Param("userId") Long userId,
            @Param("date") String date
    );
}
