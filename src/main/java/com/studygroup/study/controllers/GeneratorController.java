package com.studygroup.study.controllers;

import com.studygroup.study.enteties.*;
import com.studygroup.study.models.CourseResponse;
import com.studygroup.study.repos.*;
import com.studygroup.study.services.GeneratorService;
import com.studygroup.study.services.HelperService;
import com.studygroup.study.services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

@RestController
@RequestMapping("/api/generator")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GeneratorController {
    @Autowired UserRepo userRepo;
    @Autowired TagRepo tagRepo;
    @Autowired ArticleRepo articleRepo;
    @Autowired TaskRepo taskRepo;
    @Autowired
    CourseRepo courseRepo;

    @GetMapping("article")
    public ResponseEntity<?> generateArticle() {
        Student randomStudent = GeneratorService.randomListElement(getListOfStudents());
        String title = "Article " + GeneratorService.randomString(120);
        String text = GeneratorService.randomString(GeneratorService.randomIntBetween(1000, 2000));
        Set<Tag> tags = new HashSet<Tag>(GeneratorService.randomListElementsWithMaxCount(getListOfTags(), 5));

        Article article = new Article(title, text, randomStudent, tags);
        articleRepo.save(article);

        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @GetMapping("course-with-duration")
    public ResponseEntity<?> generateCourseWithDuration() {
        Student randomStudent = GeneratorService.randomListElement(getListOfStudents());
        String title = "Course " + GeneratorService.randomString(120);
        String description = GeneratorService.randomString(500);
        Course course = new Course(randomStudent, title, description);
        courseRepo.save(course);

        ArrayList<Task> tasks = new ArrayList<>();
        int tasksNum = GeneratorService.randomIntBetween(3, 10);

        for (int i = 0; i < tasksNum; i++) {
            String taskTitle = "Task #" + (i+1) + ' ' + GeneratorService.randomString(50);
            String taskDescription = GeneratorService.randomString(500);
            Long duration = ((Number) GeneratorService.randomIntBetween(1, 30)).longValue();

            Task newTask = new Task(
                    taskTitle,
                    taskDescription,
                    duration,
                    course.id,
                    ((Number) (i+1)).longValue()
            );

            taskRepo.save(newTask);
            tasks.add(newTask);
        }
        CourseResponse courseResponse = new CourseResponse(course, tasks);

        return new ResponseEntity<>(courseResponse, HttpStatus.OK);
    }

    @GetMapping("user")
    public ResponseEntity<?> generateUser() {
        String username = GeneratorService.randomString(10);
        String email = GeneratorService.randomString(8) + "@test.test";
        String password = GeneratorService.randomString(8);
        String firstname = GeneratorService.randomString(5);
        String lastname = GeneratorService.randomString(6);
        Character gender = GeneratorService.randomGender();
        Date birthdate = GeneratorService.randomSQLDate(1990, 2000);
        String image = "https://i.pinimg.com/564x/cb/e3/0c/cbe30c42a2c8faefbc94838f05490b25.jpg";

        Student newUser = new Student(
                username,
                email,
                password,
                firstname,
                lastname,
                gender,
                birthdate,
                image
        );
        userRepo.save(newUser);

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    private List<Student> getListOfStudents() {
        Iterable<Student> allStudents = userRepo.findAll();
        return HelperService.getListFromIterable(allStudents);
    }

    private List<Tag> getListOfTags() {
        Iterable<Tag> allTags = tagRepo.findAll();
        return HelperService.getListFromIterable(allTags);
    }
}
