package com.wayrapass.controller;

import com.wayrapass.model.Role;
import com.wayrapass.model.Student;
import com.wayrapass.model.User;
import com.wayrapass.repository.StudentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@Transactional(readOnly = true)
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public List<StudentResponse> getStudents(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return studentRepository.findAll().stream()
                .map(StudentController::toResponse)
                .toList();
    }

    private static StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getUser().getId(),
                student.getUser().getFullName(),
                student.getUser().getEmail(),
                student.getStudentCode(),
                student.getInstitution().getId(),
                student.getInstitution().getName(),
                student.getInstitution().getCampusName(),
                student.getCreatedAt()
        );
    }

    public record StudentResponse(
            Long id,
            Long userId,
            String fullName,
            String email,
            String studentCode,
            Long institutionId,
            String institutionName,
            String campusName,
            Instant createdAt
    ) {
    }
}
