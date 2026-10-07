package com.wayrapass.repository;
import com.wayrapass.model.Vehicle; import org.springframework.data.jpa.repository.*; import jakarta.persistence.LockModeType; import java.util.*;
public interface VehicleRepository extends JpaRepository<Vehicle,Long>{ boolean existsByPlateIgnoreCase(String plate); boolean existsByQrToken(String token); Optional<Vehicle> findByQrToken(String token); @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select v from Vehicle v where v.id=:id") Optional<Vehicle> findByIdForUpdate(Long id); }
