package com.wayrapass.dto.response;

import com.wayrapass.model.Role;

public record AuthResponseDTO(
        String token,
        String tokenType,
        UserResponseDTO user
) {
    public record UserResponseDTO(
            Long id,
            String fullName,
            String email,
            String phone,
            Role role
    ) {
    }
}
