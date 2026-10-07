package com.wayrapass.dto.request; import jakarta.validation.constraints.*;
public record ResetPasswordRequest(@NotBlank String token,@NotBlank @Size(min=12,max=128) String newPassword){}
