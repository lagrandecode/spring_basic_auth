package com.lagrangecode.spring_basic_authentication.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lagrangecode.spring_basic_authentication.Entity.Student;
import com.lagrangecode.spring_basic_authentication.Repository.StudentRepository;
import com.lagrangecode.spring_basic_authentication.dto.StudentRegisterDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Register {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody StudentRegisterDTO studentRegisterDTO) {
        try {
            // Check if email already exists
            if (studentRepository.findByEmail(studentRegisterDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(createErrorResponse("Email already exists"));
            }

            // Create and save new student
            Student student = new Student();
            student.setFirstName(studentRegisterDTO.getFirstName());
            student.setLastName(studentRegisterDTO.getLastName());
            student.setEmail(studentRegisterDTO.getEmail());
            student.setPassword(passwordEncoder.encode(studentRegisterDTO.getPassword()));
            student.setCourse(studentRegisterDTO.getCourse());
            student.setAge(studentRegisterDTO.getAge());
            
            Student savedStudent = studentRepository.save(student);

            // Create success response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful");
            response.put("email", savedStudent.getEmail());
            response.put("firstName", savedStudent.getFirstName());
            response.put("course", savedStudent.getCourse());
            response.put("age", savedStudent.getAge());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Invalid request data: " + e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
}
