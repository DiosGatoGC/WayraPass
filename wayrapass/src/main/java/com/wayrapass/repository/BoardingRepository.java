package com.wayrapass.repository;
import com.wayrapass.model.Boarding; import org.springframework.data.jpa.repository.JpaRepository;
public interface BoardingRepository extends JpaRepository<Boarding,Long>{ boolean existsByTripIdAndStudentId(Long tripId,Long studentId); }
