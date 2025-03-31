package ao.com.wundu.dto;

import ao.com.wundu.enums.NotificationPreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 a 100 caracteres")
        String name,

        @Pattern(
                regexp = "^(\\+244)?\\s?(9\\d{8}|2\\d{8})$",
                message = "Número de telefone inválido. Use o formato: 923456789 ou +244 923456789"
        )
        String phone,

        @NotBlank(message = "Preferência de notificação é obrigatória")
        String notificationPreference
) {
        public NotificationPreference toNotificationPreference() {
                return NotificationPreference.fromValue(notificationPreference);
        }
}
