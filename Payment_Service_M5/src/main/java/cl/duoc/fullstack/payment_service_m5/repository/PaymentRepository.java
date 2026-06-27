package cl.duoc.fullstack.payment_service_m5.repository;

import cl.duoc.fullstack.payment_service_m5.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
