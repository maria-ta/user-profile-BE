package com.studygroup.study.repos;

import com.studygroup.study.enteties.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByUsernameOrEmail(String username, String email);
    Optional<Student> findUserByUsernameAndPassword(String username, String password);
}
