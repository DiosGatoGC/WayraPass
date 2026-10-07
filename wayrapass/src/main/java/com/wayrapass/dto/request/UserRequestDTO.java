package com.wayrapass.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.wayrapass.model.Role;

public record UserRequestDTO(
        @NotBlank @Size(min = 2, max = 150) String fullName,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(min = 12, max = 128) String password,
        @Size(max = 30) String phone,
        Role role,
        Long institutionId,
        @Size(max = 40) String studentCode
) {
}
