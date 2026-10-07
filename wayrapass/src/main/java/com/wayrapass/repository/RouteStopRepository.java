package com.wayrapass.repository;
import com.wayrapass.model.RouteStop; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface RouteStopRepository extends JpaRepository<RouteStop,Long>{ boolean existsByRouteIdAndStopId(Long routeId,Long stopId); List<RouteStop> findByRouteIdOrderByStopOrder(Long routeId); }
