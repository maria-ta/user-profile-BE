package com.studygroup.study.controllers;

import com.studygroup.study.enteties.Article;
import com.studygroup.study.enteties.Student;
import com.studygroup.study.repos.ArticleRepo;
import com.studygroup.study.repos.UserRepo;
import com.studygroup.study.services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ArticleController {
    ArticleRepo articleRepo;
    UserRepo userRepo;
    LoggingService loggingService;

    public ArticleController(
            @Autowired ArticleRepo articleRepo,
            @Autowired UserRepo userRepo,
            @Autowired LoggingService loggingService
    ) {
        this.articleRepo = articleRepo;
        this.userRepo = userRepo;
        this.loggingService = loggingService;
    }

    @GetMapping("")
    public ResponseEntity<?> readArticle(
            @RequestParam("id") Long articleId
    ) {
        Optional<Article> articleOptional = this.articleRepo.findById(articleId);

        if (articleOptional.isPresent()) {
            loggingService.readArticle(articleId);
            return new ResponseEntity<>(articleOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllArticles() {
        Iterable<Article> articles = this.articleRepo.findAll();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
}
