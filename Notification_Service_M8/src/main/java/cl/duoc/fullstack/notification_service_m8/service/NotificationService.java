package cl.duoc.fullstack.notification_service_m8.service;

import cl.duoc.fullstack.notification_service_m8.model.Notification;
import cl.duoc.fullstack.notification_service_m8.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification sendNotification(Notification notification) {
        log.info("Enviando notificación de tipo [{}] al usuario ID: {}", notification.getType(), notification.getUserId());
        Notification saved = notificationRepository.save(notification);
        log.info("Notificación guardada exitosamente con ID: {}", saved.getId());
        return saved;
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}
