package com.studygroup.study.controllers;

import com.studygroup.study.enteties.Student;
import com.studygroup.study.enteties.Tag;
import com.studygroup.study.models.ActivityTiming;
import com.studygroup.study.repos.LogRepo;
import com.studygroup.study.repos.SubmissionRepo;
import com.studygroup.study.repos.TaskRepo;
import com.studygroup.study.repos.UserRepo;
import com.studygroup.study.services.GeneratorService;
import com.studygroup.study.services.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StatisticController {
    TaskRepo taskRepo;
    UserRepo userRepo;
    SubmissionRepo submissionRepo;
    LogRepo logRepo;

    StatisticController(
            @Autowired TaskRepo taskRepo,
            @Autowired UserRepo userRepo,
            @Autowired SubmissionRepo submissionRepo,
            @Autowired LogRepo logRepo
    ) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.submissionRepo = submissionRepo;
        this.logRepo = logRepo;
    }

    @GetMapping("activity")
    public ResponseEntity<?> getLoginTimes(
            @RequestParam("user") Long userId,
            @RequestParam("days") int days
    ) {
        HashMap<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        Date initialDate = new Date(System.currentTimeMillis());
        for (int i = 0; i < days; i++) {
            Date newDate = removeDaysDate(initialDate, i);
            BigDecimal count = logRepo.findActivityForDay(userId, newDate.toString());
            result.put(
                    newDate.toString(),
                    count == null ? BigDecimal.valueOf(0) : count
            );
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("rates-timing")
    public ResponseEntity<?> getAverageRatesTiming(
            @RequestParam("user") Long userId,
            @RequestParam("days") int days
    ) {
        HashMap<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        Date initialDate = new Date(System.currentTimeMillis());
        for (int i = 0; i < days; i++) {
            Date newDate = removeDaysDate(initialDate, i);
            BigDecimal rate = submissionRepo.findAverageMarkForDay(userId, newDate.toString());
            result.put(
                    newDate.toString(),
                    rate == null ? BigDecimal.valueOf(0) : rate
            );
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Date removeDaysDate(Date date, int days) {
        return Date.valueOf(date.toLocalDate().minusDays(days));
    }

    @GetMapping("average-rate")
    public ResponseEntity<?> getAverageRate(
            @RequestParam("user") Long userId
    ) {
        Date date = new Date(System.currentTimeMillis());
        BigDecimal rate = submissionRepo.findAverageMarkForDay(userId, date.toString());
        return new ResponseEntity<>(rate == null ? 0 : rate, HttpStatus.OK);
    }

    @GetMapping("discovered-topics")
    public ResponseEntity<?> getDiscoveredTopics(
            @RequestParam("user") Long userId
    ) {
        return new ResponseEntity<>(
            logRepo.findDiscoveredTags(userId).stream().map(object ->
                new Tag(((Number) object[0]).longValue(), (String) object[1], ((Number) object[2]).longValue())
            ),
            HttpStatus.OK
        );
    }

    @GetMapping("favourite-topics")
    public ResponseEntity<?> getTopFavouriteTopics(
            @RequestParam("user") Long userId
    ) {
        return new ResponseEntity<>(
                logRepo.findFavouriteTags(userId, 3).stream().map(object ->
                        new Tag(((Number) object[0]).longValue(), (String) object[1], ((Number) object[2]).longValue())
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("favourite-authors")
    public ResponseEntity<?> getTopFavouriteAuthors(
            @RequestParam("user") Long userId
    ) {
        return new ResponseEntity<>(
                logRepo.findFavouriteAuthors(userId, 3).stream().map(object ->
                        new Student(
                                ((Number) object[0]).longValue(),
                                (String) object[1],
                                (String) object[2],
                                (String) object[3],
                                (String) object[4],
                                (String) object[5],
                                (Character) object[6],
                                (Date) object[7],
                                (String) object[8]
                        )
                ),
                HttpStatus.OK
        );
    }
}
