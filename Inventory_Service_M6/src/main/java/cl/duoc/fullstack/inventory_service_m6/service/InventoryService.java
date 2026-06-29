package cl.duoc.fullstack.inventory_service_m6.service;

import cl.duoc.fullstack.inventory_service_m6.dto.InventoryResponse;
import cl.duoc.fullstack.inventory_service_m6.dto.ProductDTO;
import cl.duoc.fullstack.inventory_service_m6.model.Inventory;
import cl.duoc.fullstack.inventory_service_m6.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final RestTemplate restTemplate;

    public InventoryResponse updateStock(Long productId, Integer quantity) {
        try {
            restTemplate.getForObject(
                "http://localhost:8082/products/" + productId,
                ProductDTO.class
            );
        } catch (Exception e) {
            log.warn("Producto con ID {} no encontrado en product-service", productId);
            throw new IllegalArgumentException(
                "El producto con ID " + productId + " no existe en product-service."
            );
        }

        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseGet(() -> {
                Inventory newInventory = new Inventory();
                newInventory.setProductId(productId);
                return newInventory;
            });

        Integer currentStock = inventory.getStock() != null ? inventory.getStock() : 0;
        inventory.setStock(currentStock + quantity);

        Inventory saved = inventoryRepository.save(inventory);
        log.info("Stock actualizado para productId {}: nuevo stock {}", productId, saved.getStock());
        return toResponse(saved);
    }

    private InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getStock()
        );
    }
}
