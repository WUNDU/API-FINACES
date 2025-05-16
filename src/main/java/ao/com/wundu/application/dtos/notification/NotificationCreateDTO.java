package ao.com.wundu.application.dtos.notification;

import ao.com.wundu.domain.enums.NotificationPreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationCreateDTO (
        @NotBlank(message = "ID do usuário é obrigatório")
        String userId,
        @NotBlank(message = "Mensagem é obrigatória")
        String message,
        @NotNull(message = "Canal é obrigatório")
        NotificationPreference channel
) {
}
