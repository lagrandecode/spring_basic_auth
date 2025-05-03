package com.lagrangecode.spring_basic_authentication.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lagrangecode.spring_basic_authentication.Entity.Student;
import com.lagrangecode.spring_basic_authentication.Repository.StudentRepository;
import com.lagrangecode.spring_basic_authentication.dto.StudentLoginDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Login {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody StudentLoginDTO studentLoginDTO) {
        Optional<Student> optionalStudent = studentRepository.findByEmail(studentLoginDTO.getEmail());
        
        if (optionalStudent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Invalid email or password"));
        }

        Student student = optionalStudent.get();
        if (!passwordEncoder.matches(studentLoginDTO.getPassword(), student.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Invalid email or password"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("email", student.getEmail());
        response.put("firstName", student.getFirstName());
        
        return ResponseEntity.ok(response);
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
}
