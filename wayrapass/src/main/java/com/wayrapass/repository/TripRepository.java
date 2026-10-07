package com.wayrapass.repository;
import com.wayrapass.model.*; import org.springframework.data.jpa.repository.*; import jakarta.persistence.LockModeType; import java.time.Instant; import java.util.*;
public interface TripRepository extends JpaRepository<Trip,Long>{
 List<Trip> findByDriverUserId(Long userId);
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select t from Trip t where t.id=:id") Optional<Trip> findByIdForUpdate(Long id);
 @Query("select count(t)>0 from Trip t where t.driver.id=:driverId and t.status not in ('COMPLETED','CANCELLED') and t.scheduledStartAt < :end and coalesce(t.scheduledEndAt,t.scheduledStartAt) > :start and t.id<>:excludeId") boolean driverOverlaps(Long driverId,Instant start,Instant end,Long excludeId);
 @Query("select count(t)>0 from Trip t where t.vehicle.id=:vehicleId and t.status not in ('COMPLETED','CANCELLED') and t.scheduledStartAt < :end and coalesce(t.scheduledEndAt,t.scheduledStartAt) > :start and t.id<>:excludeId") boolean vehicleOverlaps(Long vehicleId,Instant start,Instant end,Long excludeId);
}
