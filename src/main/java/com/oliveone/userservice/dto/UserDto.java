package com.oliveone.userservice.dto;

import com.oliveone.userservice.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserDto {
    @Getter
    @Setter
    public static class UserRegistrationRequest {
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        private String password;

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        private String name;
    }

    @Getter
    @Builder
    public static class UserInfoResponse {
        private Long id;
        private String email;
        private String name;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static UserInfoResponse fromEntity(User user) {
            return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
        }
    }
    
}
