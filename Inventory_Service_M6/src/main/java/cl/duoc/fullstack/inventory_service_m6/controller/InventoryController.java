package cl.duoc.fullstack.inventory_service_m6.controller;

import cl.duoc.fullstack.inventory_service_m6.model.Inventory;
import cl.duoc.fullstack.inventory_service_m6.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PutMapping("/{productId}")
    public ResponseEntity<Inventory> updateStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Inventory updated = inventoryService.updateStock(productId, quantity);
        return ResponseEntity.ok(updated);
    }
}
