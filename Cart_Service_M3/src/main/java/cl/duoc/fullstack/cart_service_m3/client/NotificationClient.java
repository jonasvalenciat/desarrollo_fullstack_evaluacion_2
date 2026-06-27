package cl.duoc.fullstack.cart_service_m3.client;

import cl.duoc.fullstack.cart_service_m3.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class NotificationClient {
    private final RestClient restClient;

    public NotificationClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    public void sendNotification(String title, String message, String type, String recipient) {
        try {
            NotificationRequest request = new NotificationRequest(title, message, type, recipient);

            restClient.post()
                    .uri("/api/notifications")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Notificación enviada con éxito a '{}': {}", recipient, title);
        } catch (Exception e) {
            log.error("No se pudo enviar notificación a '{}' (Servicio externo caído): {}", recipient, e.getMessage());
        }
    }
}
