package cl.duoc.fullstack.cart_service_m3.dto;

import jakarta.validation.constraints.Email;

public record AssignUserRequest(
        @Email String userEmail
) {}
