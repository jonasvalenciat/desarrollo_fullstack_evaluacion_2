package cl.duoc.fullstack.cart_service_m3.repository;

import cl.duoc.fullstack.cart_service_m3.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    boolean existsByProductNameIgnoreCase(String productName);
    List<CartItem> findByStatusIgnoreCase(String status);
}
