package com.wayrapass.repository;
import com.wayrapass.model.*; import org.springframework.data.jpa.repository.*; import jakarta.persistence.LockModeType; import java.util.*;
public interface StudentRouteEnrollmentRepository extends JpaRepository<StudentRouteEnrollment,Long>{
 Optional<StudentRouteEnrollment> findByStudentIdAndStatusIn(Long id,Collection<EnrollmentStatus> statuses);
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select e from StudentRouteEnrollment e where e.student.id=:id and e.status in :statuses") Optional<StudentRouteEnrollment> findCurrentForUpdate(Long id,Collection<EnrollmentStatus> statuses);
 List<StudentRouteEnrollment> findByRouteIdAndStatus(Long routeId,EnrollmentStatus status);
}
