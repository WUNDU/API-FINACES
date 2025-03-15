package ao.com.wundu.dto;

import jakarta.validation.constraints.NotBlank;
import ao.com.wundu.entity.Notification;
import lombok.Data;

@Data
public class NotificationCreateDTO {

    @NotBlank(message = "A mensagem é obrigatória")
    private String message;

    @NotBlank(message = "Os dados de envio são obrigatórios")
    private String deliveryData;

    @NotBlank(message = "O ID do usuário é obrigatório")
    private String userId;

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
}