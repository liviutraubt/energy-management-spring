package org.example.authenticationservice.dto;

import jakarta.validation.constraints.NotBlank;
import org.example.authenticationservice.entity.Roles;

public record RegisterRequest(@NotBlank String username, @NotBlank String password, Roles role) {
}
