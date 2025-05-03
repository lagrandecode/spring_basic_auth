package com.lagrangecode.spring_basic_authentication.dto;


import com.lagrangecode.spring_basic_authentication.Entity.Course;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentLoginDTO {
    @Email
    private String email;
    @NotBlank
    private String password;
}
