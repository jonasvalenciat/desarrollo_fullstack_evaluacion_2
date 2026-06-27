package cl.duoc.fullstack.cart_service_m3.dto;

public record CartItemCommand(
        String productName,
        double price,
        int quantity,
        String userEmail,
        String couponCode
) {}
