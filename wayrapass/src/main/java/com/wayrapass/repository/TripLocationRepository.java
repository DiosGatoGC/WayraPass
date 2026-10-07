package com.wayrapass.repository;
import com.wayrapass.model.TripLocation; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface TripLocationRepository extends JpaRepository<TripLocation,Long>{ Optional<TripLocation> findFirstByTripIdOrderByRecordedAtDesc(Long tripId); }
