package cl.duoc.fullstack.inventory_service_m6.controller;

import cl.duoc.fullstack.inventory_service_m6.dto.InventoryResponse;
import cl.duoc.fullstack.inventory_service_m6.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventario", description = "API para gestionar el stock de productos")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Actualizar stock de un producto", description = "Actualiza el stock de un producto validando que exista en el servicio de productos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Producto no existe o cantidad invalida")
    })
    public ResponseEntity<InventoryResponse> updateStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        InventoryResponse updated = inventoryService.updateStock(productId, quantity);
        return ResponseEntity.ok(updated);
    }
}
