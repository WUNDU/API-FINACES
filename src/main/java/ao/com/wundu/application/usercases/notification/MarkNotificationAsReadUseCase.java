package ao.com.wundu.application.usercases.notification;

import ao.com.wundu.application.dtos.notification.NotificationResponseDTO;
import ao.com.wundu.domain.entities.Notification;
import ao.com.wundu.domain.exceptions.NotificationNotFoundException;
import ao.com.wundu.infrastructure.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MarkNotificationAsReadUseCase {

    private static final Logger logger = LoggerFactory.getLogger(MarkNotificationAsReadUseCase.class);

    private final NotificationRepository notificationRepository;

    public MarkNotificationAsReadUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponseDTO execute(String id) {
        logger.info("Marcando notificação como lida: {}", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Notificação não encontrada: {}", id);
                    return new NotificationNotFoundException("Notificação não encontrada");
                });

        notification.markAsRead();
        notification = notificationRepository.save(notification);

        logger.info("Notificação marcada como lida: {}", id);
        return new NotificationResponseDTO(notification);
    }
}
