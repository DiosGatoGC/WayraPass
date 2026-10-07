package com.wayrapass.dto.request;import jakarta.validation.constraints.*;
public record UpdateUserProfileRequest(@NotBlank @Size(min=2,max=150) String fullName,@Size(max=30) @Pattern(regexp="^$|^\\+?[0-9][0-9 ()-]{6,29}$",message="El teléfono no tiene un formato válido.") String phone){}
