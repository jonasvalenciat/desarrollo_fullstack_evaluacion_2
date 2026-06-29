package cl.duoc.fullstack.order_service_m4.dto;

public record OrderResponse(
        Long id,
        Long productId,
        Integer quantity,
        String status
) {}
