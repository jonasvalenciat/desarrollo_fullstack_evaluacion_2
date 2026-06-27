package cl.duoc.fullstack.cart_service_m3.dto;

import java.time.LocalDateTime;

public record CartHistoryResult(
        Long id,
        String previousStatus,
        String newStatus,
        String previousUserEmail,
        String newUserEmail,
        LocalDateTime changedAt,
        String comment
) {}
