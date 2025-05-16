package ao.com.wundu.application.dtos.notification;

import jakarta.validation.constraints.NotBlank;

public record NotificationPreferenceDTO(

        @NotBlank(message = "Preferência de notificação é obrigatória")
        String preference
) {
}
