package cl.duoc.fullstack.cart_service_m3.dto;

public record CartItemResponse(
        Long id,
        String productName,
        double price,
        int quantity,
        String couponCode,
        String status,
        UserResult user
) {}
