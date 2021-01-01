package com.studygroup.study.controllers;

import com.studygroup.study.enteties.Student;
import com.studygroup.study.enteties.Submission;
import com.studygroup.study.enteties.Task;
import com.studygroup.study.repos.SubmissionRepo;
import com.studygroup.study.repos.TaskRepo;
import com.studygroup.study.repos.UserRepo;
import com.studygroup.study.services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaskController {
    TaskRepo taskRepo;
    UserRepo userRepo;
    SubmissionRepo submissionRepo;
    LoggingService loggingService;

    TaskController(
            @Autowired TaskRepo taskRepo,
            @Autowired UserRepo userRepo,
            @Autowired SubmissionRepo submissionRepo,
            @Autowired LoggingService loggingService
    ) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.submissionRepo = submissionRepo;
        this.loggingService = loggingService;
    }

    @GetMapping("")
    public ResponseEntity<?> viewTask(
            @RequestParam("id") Long taskId
    ) {
        Optional<Task> optionalTask = taskRepo.findById(taskId);

        if (optionalTask.isPresent()) {
            return new ResponseEntity<>(optionalTask.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("submissions")
    public ResponseEntity<?> viewSubmission() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> optionalStudent = userRepo.findByUsername(username);

        if (optionalStudent.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Student student = optionalStudent.get();

        return new ResponseEntity<>(submissionRepo.findByStudent(student), HttpStatus.OK);
    }

    @GetMapping("available")
    public ResponseEntity<?> getAvailableTasks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> optionalStudent =  userRepo.findByUsername(username);

        if (optionalStudent.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                taskRepo.selectTasksFromStartedCourses(optionalStudent.get().id),
                HttpStatus.OK
        );
    }

    @GetMapping("started")
    public ResponseEntity<?> getStartedTasks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> optionalStudent =  userRepo.findByUsername(username);

        if (optionalStudent.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                taskRepo.selectStartedTasks(optionalStudent.get().id),
                HttpStatus.OK
        );
    }

    @PutMapping("start")
    public ResponseEntity<?> startTask(
            @RequestBody Map<String,Object> body
    ) {
        Long taskId = Long.valueOf(body.get("id").toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> optionalStudent =  userRepo.findByUsername(username);
        Optional<Task> optionalTask = taskRepo.findById(taskId);

        if (optionalTask.isEmpty() || optionalStudent.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Student student = optionalStudent.get();
        Task task = optionalTask.get();

        Optional<Submission> optionalSubmission = submissionRepo.findByStudentAndTask(student, task);
        if (optionalSubmission.isPresent()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Submission submission = new Submission(task, student);
        submissionRepo.save(submission);

        loggingService.startTask(taskId);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @PutMapping("finish")
    public ResponseEntity<?> finishTask(
            @RequestBody Map<String,Object> body
    ) {
        Long taskId = Long.valueOf(body.get("id").toString());
        String comment = body.get("comment").toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> optionalStudent =  userRepo.findByUsername(username);
        Optional<Task> optionalTask = taskRepo.findById(taskId);

        if (optionalTask.isEmpty() ) {
            return new ResponseEntity<>("No task", HttpStatus.NOT_FOUND);
        }
        if (optionalStudent.isEmpty()) {
            return new ResponseEntity<>("No student", HttpStatus.NOT_FOUND);
        }
        Task task = optionalTask.get();
        Student student = optionalStudent.get();

        Optional<Submission> optionalSubmission = submissionRepo.findByStudentAndTask(student, task);
        if (optionalSubmission.isEmpty()) return new ResponseEntity<>("No submission", HttpStatus.NOT_FOUND);
        Optional<Task> finalTaskOptional = taskRepo.findFirstByCourseIdOrderByOrderDesc(task.courseId);

        if (finalTaskOptional.isPresent()) {
            Task finalTask = finalTaskOptional.get();
            if (finalTask.id.equals(task.id)) {
                loggingService.finishCourse(task.courseId);
            }
        }

        Submission submission = optionalSubmission.get();
        if (submission.isFinished()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            submission.finish(comment);
            this.submissionRepo.save(submission);
            loggingService.finishTask(taskId);
            return new ResponseEntity<>(submission, HttpStatus.OK);
        }
    }

    @PutMapping("mark")
    public ResponseEntity<?> setMark(
            @RequestBody Map<String,Object> body
    ) {
        Long taskId = Long.valueOf(body.get("id").toString());
        Long mark = Long.valueOf(body.get("mark").toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> optionalStudent =  userRepo.findByUsername(username);
        Optional<Task> optionalTask = taskRepo.findById(taskId);

        if (optionalTask.isEmpty() || optionalStudent.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Task task = optionalTask.get();
        Student student = optionalStudent.get();

        Optional<Submission> optionalSubmission = submissionRepo.findByStudentAndTask(student, task);
        if (optionalSubmission.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Submission submission = optionalSubmission.get();
        submission.mark = mark;
        submissionRepo.save(submission);

        return new ResponseEntity<>(submission, HttpStatus.OK);
    }
}
