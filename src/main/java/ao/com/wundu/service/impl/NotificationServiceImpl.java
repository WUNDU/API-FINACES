package ao.com.wundu.service.impl;

import ao.com.wundu.application.dtos.NotificationCreateDTO;
import ao.com.wundu.application.dtos.NotificationResponseDTO;
import ao.com.wundu.domain.entities.Notification;
import ao.com.wundu.domain.exceptions.NotificationNotFoundException;
import ao.com.wundu.infrastructure.repositories.NotificationRepository;
import ao.com.wundu.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public NotificationResponseDTO createNotification(NotificationCreateDTO create) {
        Notification notification = new Notification(create.getUserId(), create.getMessage(), create.getDeliveryData());
        Notification saved = notificationRepository.save(notification);
        return new NotificationResponseDTO(saved);
    }

    @Override
    public List<NotificationResponseDTO> findAllNotifications() {
        return notificationRepository.findAll().stream().map(NotificationResponseDTO::new).toList();
    }

    @Override
    public void markAsRead(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notificação não encontrada"));
        notification.markAsRead();
        notificationRepository.save(notification);
    }
}