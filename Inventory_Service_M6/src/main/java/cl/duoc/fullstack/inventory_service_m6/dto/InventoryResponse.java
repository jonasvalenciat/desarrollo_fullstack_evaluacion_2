package cl.duoc.fullstack.inventory_service_m6.dto;

public record InventoryResponse(
        Long id,
        Long productId,
        Integer stock
) {}
