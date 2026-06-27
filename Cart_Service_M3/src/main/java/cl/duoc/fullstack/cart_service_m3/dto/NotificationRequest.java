package cl.duoc.fullstack.cart_service_m3.dto;

public record NotificationRequest(
        String title,
        String message,
        String type,
        String recipient
) {}
