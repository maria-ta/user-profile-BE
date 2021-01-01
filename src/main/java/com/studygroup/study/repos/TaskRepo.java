package com.studygroup.study.repos;

import com.studygroup.study.enteties.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepo extends CrudRepository<Task, Long> {
    Optional<Task> findByCourseIdAndOrder(Long courseId, Long order);
    Optional<Task> findFirstByCourseIdOrderByOrderDesc(Long courseId);

    @Query(
            value = "SELECT tasks.* FROM tasks WHERE tasks.course_id IN (SELECT DISTINCT tasks.course_id FROM tasks, user_tasks WHERE user_tasks.user_id = :userId AND user_tasks.task_id = tasks.id_task)",
            nativeQuery = true
    )
    Iterable<Task> selectTasksFromStartedCourses(
            @Param("userId") Long userId
    );

    @Query(
            value = "SELECT tasks.* FROM tasks, user_tasks WHERE user_tasks.user_id = :userId AND tasks.id_task = user_tasks.task_id",
            nativeQuery = true
    )
    Iterable<Task> selectStartedTasks(
            @Param("userId") Long userId
    );
}
