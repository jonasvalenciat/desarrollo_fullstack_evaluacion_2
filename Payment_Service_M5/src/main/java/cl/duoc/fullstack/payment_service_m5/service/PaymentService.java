package cl.duoc.fullstack.payment_service_m5.service;

import cl.duoc.fullstack.payment_service_m5.model.Payment;
import cl.duoc.fullstack.payment_service_m5.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment processPayment(Payment payment) {
        log.info("Procesando pago para la orden ID: " + payment.getOrderId());
        payment.setStatus("SUCCESS");
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Pago procesado exitosamente con ID: " + savedPayment.getId());
        return savedPayment;
    }
}
