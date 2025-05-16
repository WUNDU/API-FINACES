package ao.com.wundu.application.dtos.notification;
import ao.com.wundu.domain.entities.Notification;


public record NotificationResponseDTO(
        String id,
        String userId,
        String transactionId,
        String channel,
        String message,
        String status,
        boolean isRead,
        String createdAt,
        String sentAt
) {
    public NotificationResponseDTO(Notification notification) {
        this(
                notification.getId(),
                notification.getUser().getId(),
                notification.getTransaction().getId(),
                notification.getChannel().getValue(),
                notification.getMessage(),
                notification.getStatus(),
                notification.isRead(),
                notification.getCreatedAt() != null ? notification.getCreatedAt().toString() : null,
                notification.getSentAt() != null ? notification.getSentAt().toString() : null
        );
    }
}
