package cl.duoc.fullstack.order_service_m4;

import cl.duoc.fullstack.order_service_m4.dto.OrderRequest;
import cl.duoc.fullstack.order_service_m4.dto.OrderResponse;
import cl.duoc.fullstack.order_service_m4.dto.ProductDTO;
import cl.duoc.fullstack.order_service_m4.model.Order;
import cl.duoc.fullstack.order_service_m4.repository.OrderRepository;
import cl.duoc.fullstack.order_service_m4.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private RestTemplate restTemplate;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, restTemplate);
    }

    @Test
    void createOrder_WithValidProduct_ShouldReturnOrderResponse() {
        // Given
        OrderRequest request = new OrderRequest(1L, 2);
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Laptop");
        Order saved = new Order();
        saved.setId(1L);
        saved.setProductId(1L);
        saved.setQuantity(2);
        saved.setStatus("CREATED");

        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(product);
        when(orderRepository.save(any())).thenReturn(saved);

        // When
        OrderResponse response = orderService.createOrder(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.productId());
        assertEquals(2, response.quantity());
        assertEquals("CREATED", response.status());
        verify(orderRepository).save(any());
    }

    @Test
    void createOrder_WithNonExistentProduct_ShouldThrowException() {
        // Given
        OrderRequest request = new OrderRequest(999L, 1);
        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class)))
                .thenThrow(new RuntimeException("Product not found"));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(request));
        assertTrue(exception.getMessage().contains("El producto con ID 999 no existe"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrderById_WhenExists_ShouldReturnOrderResponse() {
        // Given
        Order order = new Order();
        order.setId(1L);
        order.setProductId(1L);
        order.setQuantity(3);
        order.setStatus("CREATED");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        OrderResponse response = orderService.getOrderById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(1L, response.productId());
        assertEquals(3, response.quantity());
    }

    @Test
    void getOrderById_WhenNotExists_ShouldThrowEntityNotFoundException() {
        // Given
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrderById(999L));
        assertTrue(exception.getMessage().contains("Orden no encontrada con ID 999"));
    }
}
