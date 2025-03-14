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
}