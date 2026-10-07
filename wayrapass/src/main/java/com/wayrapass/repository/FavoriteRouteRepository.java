package com.wayrapass.repository;
import com.wayrapass.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute,FavoriteRouteId>{ List<FavoriteRoute> findByStudentId(Long studentId); }
