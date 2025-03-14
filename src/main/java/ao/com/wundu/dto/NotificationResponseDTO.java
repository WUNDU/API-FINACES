package ao.com.wundu.dto;
import ao.com.wundu.entity.Notification;
import lombok.Data;

@Data
public class NotificationResponseDTO {
    private String id;
    private String message;
    private String deliveryData;
    private String userId;
    private boolean read;

    public NotificationResponseDTO(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.deliveryData = notification.getDeliveryData();
        this.userId = notification.getUserId();
        this.read = notification.isRead();
    }
}
