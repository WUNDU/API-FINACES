package ao.com.wundu.application.dtos;
import ao.com.wundu.domain.entities.Notification;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeliveryData() {
        return deliveryData;
    }

    public void setDeliveryData(String deliveryData) {
        this.deliveryData = deliveryData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
