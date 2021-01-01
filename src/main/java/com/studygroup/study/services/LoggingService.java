package com.studygroup.study.services;

import com.studygroup.study.enteties.Log;
import com.studygroup.study.enteties.Student;
import com.studygroup.study.enums.LogStatus;
import com.studygroup.study.repos.LogRepo;
import com.studygroup.study.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoggingService {
    @Autowired LogRepo logRepo;
    @Autowired UserRepo userRepo;

    public void login() {
        createLog(LogStatus.LOGIN);
    }

    public void logout() {
        createLog(LogStatus.LOGOUT);
    }

    public void readArticle(Long articleId) {
        createLogForEntity(articleId, LogStatus.READ_ARTICLE);
    }

    public void startCourse(Long courseId) {
        createLogForEntity(courseId, LogStatus.START_COURSE);
    }

    public void finishCourse(Long courseId) {
        createLogForEntity(courseId, LogStatus.FINISH_COURSE);
    }

    public void startTask(Long taskId) {
        createLogForEntity(taskId, LogStatus.START_TASK);
    }

    public void finishTask(Long taskId) {
        createLogForEntity(taskId, LogStatus.FINISH_TASK);
    }

    private void createLog(LogStatus status) {
        Optional<Student> optionalStudent = getStudent();
        if (optionalStudent.isPresent()) {
            Long userId = optionalStudent.get().id;
            Log log = new Log(userId, status);
            logRepo.save(log);
        }
    }

    private void createLogForEntity(Long entityId, LogStatus status) {
        Optional<Student> optionalStudent = getStudent();
        if (optionalStudent.isPresent()) {
            Long userId = optionalStudent.get().id;
            Log log = new Log(userId, entityId, status);
            logRepo.save(log);
        }
    }

    public Optional<Student> getStudent() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username);
    }
}
