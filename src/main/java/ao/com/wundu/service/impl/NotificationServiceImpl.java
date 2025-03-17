//package ao.com.wundu.service.impl;
//
//import ao.com.wundu.dto.NotificationCreateDTO;
//import ao.com.wundu.dto.NotificationResponseDTO;
//import ao.com.wundu.entity.Notification;
//import ao.com.wundu.repository.NotificationRepository;
//import ao.com.wundu.service.NotificationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.http.HttpStatus;
//import java.util.List;
//
//@Service
//public class NotificationServiceImpl implements NotificationService {
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Override
//    public NotificationResponseDTO createNotification(NotificationCreateDTO create) {
//        Notification notification = new Notification(create.getUserId(), create.getMessage(), create.getDeliveryData());
//        Notification saved = notificationRepository.save(notification);
//        return new NotificationResponseDTO(saved);
//    }
//
//    @Override
//    public List<NotificationResponseDTO> findAllNotifications() {
//        return notificationRepository.findAll().stream().map(NotificationResponseDTO::new).toList();
//    }
//
//    @Override
//    public void markAsRead(String id) {
//        Notification notification = notificationRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));
//        notification.markAsRead();
//        notificationRepository.save(notification);
//    }
//}