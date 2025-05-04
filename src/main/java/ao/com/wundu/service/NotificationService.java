package ao.com.wundu.service;

import ao.com.wundu.application.dtos.NotificationCreateDTO;
import ao.com.wundu.application.dtos.NotificationResponseDTO;
import java.util.List;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationCreateDTO create);
    List<NotificationResponseDTO> findAllNotifications();
    void markAsRead(String id);
}
