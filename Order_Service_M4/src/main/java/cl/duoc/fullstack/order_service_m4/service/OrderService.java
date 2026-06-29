package cl.duoc.fullstack.order_service_m4.service;

import cl.duoc.fullstack.order_service_m4.dto.OrderRequest;
import cl.duoc.fullstack.order_service_m4.dto.OrderResponse;
import cl.duoc.fullstack.order_service_m4.dto.ProductDTO;
import cl.duoc.fullstack.order_service_m4.model.Order;
import cl.duoc.fullstack.order_service_m4.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderResponse createOrder(OrderRequest request) {
        try {
            restTemplate.getForObject(
                    "http://localhost:8082/products/" + request.productId(),
                    ProductDTO.class
            );
        } catch (Exception e) {
            log.warn("El producto con ID {} no existe.", request.productId());
            throw new IllegalArgumentException("El producto con ID " + request.productId() + " no existe.");
        }

        Order order = new Order();
        order.setProductId(request.productId());
        order.setQuantity(request.quantity());
        order.setStatus("CREATED");
        Order saved = orderRepository.save(order);
        log.info("Orden creada exitosamente con ID {}", saved.getId());
        return toResponse(saved);
    }

    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada con ID " + id));
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getStatus()
        );
    }
}
