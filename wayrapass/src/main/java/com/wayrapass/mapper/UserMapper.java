package com.wayrapass.mapper;
import com.wayrapass.dto.response.AuthResponseDTO; import com.wayrapass.model.User; import org.mapstruct.Mapper;
@Mapper(componentModel="spring") public interface UserMapper { AuthResponseDTO.UserResponseDTO toResponse(User user); }
