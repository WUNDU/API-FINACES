package ao.com.wundu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Notification {
    @Id
    @GeneratedValue
    private final String id;
    private final String userId;
    private final String message;
    private final String deliveryData;
    private boolean isRead;
    private final LocalDateTime createdAt;

    public Notification(String userId, String message, String deliveryData) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("ID do usuário é obrigatório");
        if (message == null || message.isBlank()) throw new IllegalArgumentException("Mensagem é obrigatória");
        if (deliveryData == null || deliveryData.isBlank()) throw new IllegalArgumentException("Dados de envio são obrigatórios");

        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.message = message;
        this.deliveryData = deliveryData;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsRead() {
        if (isRead) throw new IllegalStateException("Notificação já está marcada como lida");
        this.isRead = true;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getDeliveryData() { return deliveryData; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}