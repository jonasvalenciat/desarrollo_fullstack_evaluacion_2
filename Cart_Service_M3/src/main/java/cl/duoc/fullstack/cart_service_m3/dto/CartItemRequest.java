package cl.duoc.fullstack.cart_service_m3.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CartItemRequest(
        @NotBlank(message = "El nombre del producto es requerido")
        @Size(min = 2, max = 50)
        String productName,

        double price,
        int quantity,

        @NotBlank(message = "El email del usuario es requerido")
        @Email(message = "Formato de email inválido")
        String userEmail,

        String couponCode
) {}
