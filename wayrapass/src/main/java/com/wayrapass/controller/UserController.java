package com.wayrapass.controller;

import com.wayrapass.model.User;
import com.wayrapass.repository.UserRepository;
import com.wayrapass.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestController
@RequestMapping("/api/users")
@Transactional(readOnly = true)
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserController(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @GetMapping("/me")
    public UserProfileResponse getMyProfile(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return toResponse(authService.requireAuthenticatedUser(authorization));
    }

    @PutMapping("/me")
    @Transactional
    public UserProfileResponse updateMyProfile(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        User user = authService.requireAuthenticatedUser(authorization);
        String phone = request.phone().trim();
        if (userRepository.existsByPhoneAndIdNot(phone, user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El teléfono ya está registrado.");
        }

        user.setFullName(request.fullName().trim());
        user.setPhone(phone);
        return toResponse(userRepository.save(user));
    }

    private static UserProfileResponse toResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public record UpdateUserProfileRequest(
            @NotBlank @Size(min = 2, max = 120) String fullName,
            @NotBlank @Size(max = 30)
            @Pattern(regexp = "^\\+?[0-9][0-9 ()-]{6,29}$", message = "El teléfono no tiene un formato válido.")
            String phone
    ) {
    }

    public record UserProfileResponse(
            Long id,
            String fullName,
            String email,
            String phone,
            com.wayrapass.model.Role role,
            boolean enabled,
            Instant createdAt,
            Instant updatedAt
    ) {
    }
}
