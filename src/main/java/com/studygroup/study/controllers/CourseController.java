package com.studygroup.study.controllers;

import com.studygroup.study.enteties.Course;
import com.studygroup.study.enteties.Student;
import com.studygroup.study.enteties.Submission;
import com.studygroup.study.enteties.Task;
import com.studygroup.study.repos.CourseRepo;
import com.studygroup.study.repos.SubmissionRepo;
import com.studygroup.study.repos.TaskRepo;
import com.studygroup.study.repos.UserRepo;
import com.studygroup.study.services.HelperService;
import com.studygroup.study.services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CourseController {
    CourseRepo courseRepo;
    UserRepo userRepo;
    TaskRepo taskRepo;
    SubmissionRepo submissionRepo;
    LoggingService loggingService;

    CourseController(
            @Autowired CourseRepo courseRepo,
            @Autowired UserRepo userRepo,
            @Autowired TaskRepo taskRepo,
            @Autowired SubmissionRepo submissionRepo,
            @Autowired LoggingService loggingService
            ) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.submissionRepo = submissionRepo;
        this.loggingService = loggingService;
    }

    @GetMapping("")
    public ResponseEntity<?> viewCourse(
            @RequestParam("id") Long courseId
    ) {
        Optional<Course> optionalCourse = courseRepo.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalCourse.get(), HttpStatus.OK);
    }

    @PostMapping("start")
    public ResponseEntity<?> startCourse(
            @RequestBody Map<String,Object> body
    ) {
        Long courseId = Long.valueOf(body.get("id").toString());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> optionalStudent =  userRepo.findByUsername(username);
        Optional<Course> optionalCourse = courseRepo.findById(courseId);

        if (optionalCourse.isEmpty() || optionalStudent.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Course course = optionalCourse.get();
            Student student = optionalStudent.get();

            Optional<Task> optionalTask = taskRepo.findByCourseIdAndOrder(course.id, (long) 1.0);
            if (optionalTask.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            Optional<Submission> optionalSubmission = submissionRepo.findByStudentAndTask(student, optionalTask.get());
            if (optionalSubmission.isPresent()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            Submission submission = new Submission(optionalTask.get(), student);
            submissionRepo.save(submission);

            loggingService.startCourse(courseId);
            loggingService.startTask(optionalTask.get().id);

            return new ResponseEntity<>(submission, HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCourses() {
        Iterable<Course> courses = courseRepo.findAll();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/started/statistic")
    public ResponseEntity<?> getStartedCoursesWithCompletedNum() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> optionalStudent =  userRepo.findByUsername(username);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            Iterable<Object[]> courses = courseRepo.findAllStartedCoursesWithCompletedNums(student.id);
            Object[] result = HelperService.getListFromIterable(courses)
                    .stream()
                    .map(obj -> {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", Integer.toString( (int) obj[0]));
                        map.put("title", (String) obj[1]);
                        map.put("description", (String) obj[2]);
                        map.put("finished", Integer.toString( (int) obj[3]));
                        map.put("planned", ((BigInteger) obj[4]).toString());
                        return map;
                    }).toArray();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/started")
    public ResponseEntity<?> getStartedCourses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Student> optionalStudent =  userRepo.findByUsername(username);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            Iterable<Course> courses = courseRepo.findAllStartedCourses(student.id);

            return new ResponseEntity<>(courses, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
