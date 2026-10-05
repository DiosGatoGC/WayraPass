package com.wayrapass.controller;

import com.wayrapass.model.Role;
import com.wayrapass.model.Student;
import com.wayrapass.model.User;
import com.wayrapass.repository.StudentRepository;
import com.wayrapass.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@Transactional(readOnly = true)
public class StudentController {

    private final StudentRepository studentRepository;
    private final AuthService authService;

    public StudentController(StudentRepository studentRepository, AuthService authService) {
        this.studentRepository = studentRepository;
        this.authService = authService;
    }

    @GetMapping
    public List<StudentResponse> getStudents(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        requireAdmin(authService.requireAuthenticatedUser(authorization));
        return studentRepository.findAll().stream()
                .map(StudentController::toResponse)
                .toList();
    }

    private static void requireAdmin(User user) {
        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta operación requiere una cuenta administradora.");
        }
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
