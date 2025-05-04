package ao.com.wundu.application.dtos.user;

import ao.com.wundu.domain.enums.NotificationPreference;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "Nome não pode estar em branco")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String name,

        @NotBlank(message = "Email não pode estar em branco")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha não pode estar em branco")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "Senha deve conter pelo menos uma letra maiúscula e um número")
        String password,

        @NotBlank(message = "Telefone não pode estar em branco")
        @Pattern(regexp = "^(\\+244)?\\s?(9\\d{8}|2\\d{8})$", message = "Telefone inválido. Exemplo: +244923456789")
        String phone,

        @NotBlank(message = "Preferência de notificação é obrigatória")
        String notificationPreference
) {
        public NotificationPreference toNotificationPreference() {
                return NotificationPreference.fromValue(notificationPreference);
        }
}
