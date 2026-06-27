package cl.duoc.fullstack.order_service_m4.service;

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

    public Order createOrder(Order order) {
        try {
            restTemplate.getForObject(
                    "http://localhost:8082/products/" + order.getProductId(),
                    ProductDTO.class
            );
        } catch (Exception e) {
            log.warn("El producto con ID {} no existe.", order.getProductId());
            throw new IllegalArgumentException("El producto con ID " + order.getProductId() + " no existe.");
        }

        order.setStatus("CREATED");
        Order saved = orderRepository.save(order);
        log.info("Orden creada exitosamente con ID {}", saved.getId());
        return saved;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada con ID " + id));
    }
}
