package com.studygroup.study.repos;

import com.studygroup.study.enteties.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface CourseRepo extends CrudRepository<Course, Long> {
    @Query(
            value = "SELECT courses.*, ( SELECT COUNT(tasks.id_task) FROM tasks, user_tasks, courses WHERE user_tasks.datetime_finished IS NOT Null AND user_tasks.task_id = tasks.id_task AND tasks.course_id = courses.id_course AND user_tasks.user_id = :userId ) as finished, ( SELECT COUNT(tasks.id_task) FROM tasks, courses WHERE tasks.course_id = courses.id_course ) as planned FROM courses, tasks, user_tasks WHERE tasks.course_id = courses.id_course AND user_tasks.task_id = tasks.id_task AND user_tasks.user_id = :userId",
            nativeQuery = true
    )
    Iterable<Object[]> findAllStartedCoursesWithCompletedNums(
            @Param("userId") Long userId
    );

    @Query(
            value = "SELECT DISTINCT courses.* FROM courses, tasks, user_tasks WHERE user_tasks.user_id = :userId AND user_tasks.task_id = tasks.id_task AND tasks.course_id = courses.id_course",
            nativeQuery = true
    )
    Iterable<Course> findAllStartedCourses(
            @Param("userId") Long userId
    );
}
