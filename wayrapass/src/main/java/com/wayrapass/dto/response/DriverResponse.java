package com.wayrapass.dto.response;
import com.wayrapass.model.*; import java.time.*;
public record DriverResponse(Long id,Long userId,String fullName,String email,String phone,String licenseNumber,String licenseCategory,LocalDate licenseExpiration,String credentialImageUrl,AuthorizationStatus authorizationStatus,DriverOperationalStatus operationalStatus,Instant createdAt,Instant updatedAt){}
