package cl.duoc.fullstack.order_service_m4.repository;

import cl.duoc.fullstack.order_service_m4.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
