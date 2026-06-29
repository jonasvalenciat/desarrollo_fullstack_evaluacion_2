package cl.duoc.fullstack.inventory_service_m6;

import cl.duoc.fullstack.inventory_service_m6.dto.InventoryResponse;
import cl.duoc.fullstack.inventory_service_m6.dto.ProductDTO;
import cl.duoc.fullstack.inventory_service_m6.model.Inventory;
import cl.duoc.fullstack.inventory_service_m6.repository.InventoryRepository;
import cl.duoc.fullstack.inventory_service_m6.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private RestTemplate restTemplate;

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService(inventoryRepository, restTemplate);
    }

    @Test
    void updateStock_WithNewProduct_ShouldCreateAndReturnStock() {
        // Given
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Laptop");

        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(product);
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.empty());

        Inventory saved = new Inventory(1L, 1L, 10);
        when(inventoryRepository.save(any())).thenReturn(saved);

        // When
        InventoryResponse response = inventoryService.updateStock(1L, 10);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.productId());
        assertEquals(10, response.stock());
        verify(inventoryRepository).save(any());
    }

    @Test
    void updateStock_WithExistingProduct_ShouldAccumulateStock() {
        // Given
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Laptop");

        Inventory existing = new Inventory(1L, 1L, 5);

        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(product);
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(existing));

        Inventory saved = new Inventory(1L, 1L, 15);
        when(inventoryRepository.save(any())).thenReturn(saved);

        // When
        InventoryResponse response = inventoryService.updateStock(1L, 10);

        // Then
        assertEquals(15, response.stock());
    }

    @Test
    void updateStock_WithNonExistentProduct_ShouldThrowException() {
        // Given
        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class)))
                .thenThrow(new RuntimeException("Product not found"));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inventoryService.updateStock(999L, 5));
        assertTrue(exception.getMessage().contains("El producto con ID 999 no existe"));
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void updateStock_WithNegativeQuantity_ShouldDecreaseStock() {
        // Given
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Laptop");

        Inventory existing = new Inventory(1L, 1L, 20);

        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(product);
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(existing));

        Inventory saved = new Inventory(1L, 1L, 15);
        when(inventoryRepository.save(any())).thenReturn(saved);

        // When
        InventoryResponse response = inventoryService.updateStock(1L, -5);

        // Then
        assertEquals(15, response.stock());
    }
}
