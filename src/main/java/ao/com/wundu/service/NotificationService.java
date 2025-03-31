package ao.com.wundu.service;

import ao.com.wundu.dto.NotificationCreateDTO;
import ao.com.wundu.dto.NotificationResponseDTO;
import java.util.List;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationCreateDTO create);
    List<NotificationResponseDTO> findAllNotifications();
    void markAsRead(String id);
}
