package com.lagrangecode.spring_basic_authentication.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lagrangecode.spring_basic_authentication.Entity.Course;
import com.lagrangecode.spring_basic_authentication.Entity.Student;
import com.lagrangecode.spring_basic_authentication.Repository.StudentRepository;
import com.lagrangecode.spring_basic_authentication.dto.StudentResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            return ResponseEntity.ok(convertToResponseDTO(studentOpt.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Student not found"));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        List<StudentResponseDTO> students = studentRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudentById(@PathVariable Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isPresent()) {
            return ResponseEntity.ok(convertToResponseDTO(studentOpt.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Student not found"));
    }

    @PutMapping("/me")
    public ResponseEntity<Object> updateCurrentStudent(@RequestBody Map<String, Object> updates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (updates.containsKey("firstName")) {
                student.setFirstName((String) updates.get("firstName"));
            }
            if (updates.containsKey("lastName")) {
                student.setLastName((String) updates.get("lastName"));
            }
            if (updates.containsKey("phoneNumber")) {
                student.setPhoneNumber((String) updates.get("phoneNumber"));
            }
            if (updates.containsKey("age")) {
                student.setAge((Integer) updates.get("age"));
            }
            if (updates.containsKey("course")) {
                student.setCourse((Course) updates.get("course"));
            }
            
            Student updatedStudent = studentRepository.save(student);
            return ResponseEntity.ok(convertToResponseDTO(updatedStudent));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Student not found"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Object> deleteCurrentStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            studentRepository.delete(studentOpt.get());
            return ResponseEntity.ok(Map.of("message", "Student account deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Student not found"));
    }

    private StudentResponseDTO convertToResponseDTO(Student student) {
        return new StudentResponseDTO(
                student.getFirstName(),
                student.getEmail(),
                student.getCourse(),
                student.getPhoneNumber(),
                student.getAge()
        );
    }
}
