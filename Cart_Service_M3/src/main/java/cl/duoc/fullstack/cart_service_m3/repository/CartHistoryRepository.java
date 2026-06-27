package cl.duoc.fullstack.cart_service_m3.repository;

import cl.duoc.fullstack.cart_service_m3.model.CartHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartHistoryRepository extends JpaRepository<CartHistory, Long> {
    List<CartHistory> findByCartItemIdOrderByChangedAtDesc(Long cartItemId);
}
