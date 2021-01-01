package com.studygroup.study.models;

import com.studygroup.study.enteties.Course;
import com.studygroup.study.enteties.Task;

import java.io.Serializable;
import java.util.List;

public class CourseResponse implements Serializable {
    public Course course;
    public List<Task> tasks;

    public CourseResponse(Course course, List<Task> tasks) {
        this.course = course;
        this.tasks = tasks;
    }
}
