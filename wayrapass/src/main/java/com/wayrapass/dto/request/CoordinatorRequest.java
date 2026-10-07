package com.wayrapass.dto.request;import jakarta.validation.constraints.*;
public record CoordinatorRequest(@NotBlank @Size(max=150) String fullName,@NotBlank @Email @Size(max=150) String email,@NotBlank @Size(min=12,max=128) String password,@Size(max=30) String phone){}
