package com.studygroup.study.repos;

import com.studygroup.study.enteties.Student;
import com.studygroup.study.enteties.Submission;
import com.studygroup.study.enteties.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface SubmissionRepo extends CrudRepository<Submission, Long> {
    Optional<Submission> findByStudentAndTask(Student student, Task task);
    Iterable<Submission> findByStudent(Student student);

    @Query(
            value = "SELECT AVG(user_tasks.mark) as mark FROM user_tasks WHERE user_tasks.user_id = :userId AND DATE(user_tasks.datetime_finished) <= :date",
            nativeQuery = true
    )
    BigDecimal findAverageMarkForDay(
            @Param("userId") Long userId,
            @Param("date") String date
    );
}
