package com.wayrapass.repository;
import com.wayrapass.model.Institution; import org.springframework.data.jpa.repository.JpaRepository;
public interface InstitutionRepository extends JpaRepository<Institution,Long>{ boolean existsByNameIgnoreCaseAndCampusNameIgnoreCase(String name,String campus); }
