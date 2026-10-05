package com.wayrapass.repository;

import com.wayrapass.model.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "institution"})
    List<Student> findAll();
}