package com.wayrapass.repository;

import com.wayrapass.model.Driver;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByLicenseNumberIgnoreCase(String licenseNumber);

    boolean existsByLicenseNumberIgnoreCaseAndIdNot(String licenseNumber, Long id);

    boolean existsByUserId(Long userId);

    Optional<Driver> findByUserId(Long userId);

    @EntityGraph(attributePaths = "user")
    List<Driver> findAllByOrderByIdAsc();
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select d from Driver d where d.id=:id") Optional<Driver> findByIdForUpdate(Long id);
}
