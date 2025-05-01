package com.lagrangecode.spring_basic_authentication.dto;

import com.lagrangecode.spring_basic_authentication.Entity.Course;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StudentRegisterDTO {
    @NotBlank
    private String firstName;
    @Email
    private String email;
    @NotBlank
    private String password;

    private Course coursep;
    private String level;
    private int age;



}
