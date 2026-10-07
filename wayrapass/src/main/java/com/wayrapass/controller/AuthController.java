package com.wayrapass.controller;

import com.wayrapass.dto.request.LoginRequestDTO;
import com.wayrapass.dto.request.UserRequestDTO;
import com.wayrapass.dto.request.ForgotPasswordRequest;
import com.wayrapass.dto.request.ResetPasswordRequest;
import com.wayrapass.dto.response.AuthResponseDTO;
import com.wayrapass.dto.response.PasswordResetResponse;
import com.wayrapass.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO register(@Valid @RequestBody UserRequestDTO request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/forgot-password")
    public PasswordResetResponse forgot(@Valid @RequestBody ForgotPasswordRequest request) { return authService.forgotPassword(request); }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reset(@Valid @RequestBody ResetPasswordRequest request) { authService.resetPassword(request); }
}
