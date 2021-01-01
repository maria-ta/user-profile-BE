package com.studygroup.study.controllers;

import com.studygroup.study.enteties.Student;
import com.studygroup.study.repos.ArticleRepo;
import com.studygroup.study.repos.UserRepo;
import com.studygroup.study.services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    ArticleRepo articleRepo;
    UserRepo userRepo;
    LoggingService loggingService;

    public UserController(
            @Autowired ArticleRepo articleRepo,
            @Autowired UserRepo userRepo,
            @Autowired LoggingService loggingService
    ) {
        this.articleRepo = articleRepo;
        this.userRepo = userRepo;
        this.loggingService = loggingService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUser(
            @RequestParam("user") Long userId
    ) {
        Optional<Student> user = userRepo.findById(userId);
        return user.isPresent() ?
                new ResponseEntity<>(user.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("registration")
    public ResponseEntity<?> createUser(
            @RequestBody Map<String,Object> body
    ) {
        Optional<Student> user = userRepo.findByUsernameOrEmail(
                body.get("username").toString(),
                body.get("email").toString()
        );

        if (user.isEmpty()) {
            Student student = new Student(
                    body.get("username").toString(),
                    body.get("email").toString(),
                    body.get("password").toString()
            );
            userRepo.save(student);
            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User already exists", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUser(
            @RequestParam("user") Long userId
    ) {
        Optional<Student> user = userRepo.findById(userId);
        if (user.isPresent()) {
            userRepo.delete(user.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(
            @RequestBody Map<String,Object> body
    ) {
        Optional<Student> optionalStudent = loggingService.getStudent();

        if (optionalStudent.isPresent()) {
            Student user = optionalStudent.get();
            Date date = Date.valueOf(
                    body.get("birthdate").toString()
            );

            user.email = body.get("email").toString();
            user.firstname = body.get("firstname").toString();
            user.lastname = body.get("lastname").toString();
            user.gender = body.get("gender").toString().charAt(0);
            user.birthdate = date;
            user.image = body.get("image").toString();

            userRepo.save(user);

            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("current")
    public ResponseEntity<?> getCurrentUser() {
        Optional<Student> user = loggingService.getStudent();

        return user.isPresent() ?
                new ResponseEntity<>(user.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("logout")
    public ResponseEntity<?> logout() {
        Optional<Student> optionalUser = loggingService.getStudent();

        if (optionalUser.isPresent()) {
            loggingService.logout();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null){
                new SecurityContextLogoutHandler().setInvalidateHttpSession(true);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
