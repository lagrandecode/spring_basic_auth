package com.lagrangecode.spring_basic_authentication.Entity;

public enum Course {
    MATHEMATICS("Mathematics"),
    ENGLISH("English");

    private final String courseName;

    // Constructor
    Course(String courseName) {
        this.courseName = courseName;
    }

    // Getter method for the course name
    public String getCourseName() {
        return this.courseName;
    }
}
