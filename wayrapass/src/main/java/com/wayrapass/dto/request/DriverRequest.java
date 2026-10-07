package com.wayrapass.dto.request;
import com.wayrapass.model.*; import jakarta.validation.constraints.*; import java.time.LocalDate;
public record DriverRequest(@NotBlank @Size(max=150) String fullName,@NotBlank @Email @Size(max=150) String email,@Size(min=12,max=128) String password,@Size(max=30) String phone,@NotBlank @Size(max=50) String licenseNumber,@NotBlank @Size(max=20) String licenseCategory,LocalDate licenseExpiration,@Size(max=500) String credentialImageUrl,@NotNull AuthorizationStatus authorizationStatus,@NotNull DriverOperationalStatus operationalStatus){}
