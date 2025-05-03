package com.lagrangecode.spring_basic_authentication.dto;

import com.lagrangecode.spring_basic_authentication.Entity.Course;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentResponseDTO {
    private String firstName;
    private String email;
    private Course course;
    private String phoneNumber;
    private int age;
}
