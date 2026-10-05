package com.wayrapass.controller;

import com.wayrapass.model.Driver;
import com.wayrapass.model.Role;
import com.wayrapass.model.User;
import com.wayrapass.repository.DriverRepository;
import com.wayrapass.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/drivers")
@Transactional(readOnly = true)
public class DriverController {

    private final DriverRepository driverRepository;
    private final AuthService authService;

    public DriverController(DriverRepository driverRepository, AuthService authService) {
        this.driverRepository = driverRepository;
        this.authService = authService;
    }

    @GetMapping
    public List<DriverProfileResponse> getDrivers(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        requireAdmin(authService.requireAuthenticatedUser(authorization));
        return driverRepository.findAllByOrderByIdAsc().stream()
                .map(DriverController::toResponse)
                .toList();
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public DriverProfileResponse createMyProfile(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody DriverProfileRequest request
    ) {
        User user = requireDriver(authService.requireAuthenticatedUser(authorization));
        if (driverRepository.existsByUserId(user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El perfil de conductor ya existe.");
        }

        String licenseNumber = normalizeLicenseNumber(request.licenseNumber());
        if (driverRepository.existsByLicenseNumberIgnoreCase(licenseNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de licencia ya está registrado.");
        }

        Driver driver = new Driver();
        driver.setUser(user);
        applyProfile(driver, request, licenseNumber);
        return toResponse(driverRepository.save(driver));
    }

    @GetMapping("/me")
    public DriverProfileResponse getMyProfile(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        User user = requireDriver(authService.requireAuthenticatedUser(authorization));
        Driver driver = driverRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un perfil de conductor."));
        return toResponse(driver);
    }

    @PutMapping("/me")
    @Transactional
    public DriverProfileResponse updateMyProfile(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody DriverProfileRequest request
    ) {
        User user = requireDriver(authService.requireAuthenticatedUser(authorization));
        Driver driver = driverRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un perfil de conductor."));

        String licenseNumber = normalizeLicenseNumber(request.licenseNumber());
        if (driverRepository.existsByLicenseNumberIgnoreCaseAndIdNot(licenseNumber, driver.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El número de licencia ya está registrado.");
        }

        applyProfile(driver, request, licenseNumber);
        return toResponse(driverRepository.save(driver));
    }

    private static User requireDriver(User user) {
        if (user.getRole() != Role.DRIVER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta operación requiere una cuenta de conductor.");
        }
        return user;
    }

    private static void requireAdmin(User user) {
        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta operación requiere una cuenta administradora.");
        }
    }

    private static void applyProfile(Driver driver, DriverProfileRequest request, String licenseNumber) {
        driver.setLicenseNumber(licenseNumber);
        driver.setCredentialImageUrl(
                request.credentialImageUrl() == null ? null : request.credentialImageUrl().trim()
        );
        driver.setLicenseExpiration(request.licenseExpiration());
        driver.setLicenseCategory(request.licenseCategory().trim());
    }

    private static String normalizeLicenseNumber(String licenseNumber) {
        return licenseNumber.trim().toUpperCase(Locale.ROOT);
    }

    private static DriverProfileResponse toResponse(Driver driver) {
        return new DriverProfileResponse(
                driver.getId(),
                driver.getLicenseNumber(),
                driver.getCredentialImageUrl(),
                driver.getLicenseExpiration(),
                driver.getLicenseCategory(),
                driver.getAuthorizationStatus(),
                driver.getOperationalStatus(),
                driver.getCreatedAt(),
                driver.getUpdatedAt()
        );
    }

    public record DriverProfileRequest(
            @NotBlank @Size(min = 4, max = 120) String licenseNumber,
            @Size(max = 500) String credentialImageUrl,
            @NotNull @Future LocalDate licenseExpiration,
            @NotBlank @Size(max = 100) String licenseCategory
    ) {
    }

    public record DriverProfileResponse(
            Long id,
            String licenseNumber,
            String credentialImageUrl,
            LocalDate licenseExpiration,
            String licenseCategory,
            String authorizationStatus,
            String operationalStatus,
            Instant createdAt,
            Instant updatedAt
    ) {
    }
}
