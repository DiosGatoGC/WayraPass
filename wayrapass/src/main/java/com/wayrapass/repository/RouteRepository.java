package com.wayrapass.repository;
import com.wayrapass.model.Route; import org.springframework.data.jpa.repository.JpaRepository;
public interface RouteRepository extends JpaRepository<Route,Long>{ boolean existsByCodeIgnoreCase(String code); }
