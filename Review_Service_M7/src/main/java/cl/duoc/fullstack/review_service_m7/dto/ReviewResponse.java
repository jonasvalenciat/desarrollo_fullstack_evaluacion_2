package cl.duoc.fullstack.review_service_m7.dto;

public record ReviewResponse(
        Long id,
        Long productId,
        Long userId,
        Integer rating,
        String comment
) {}
