package ao.com.wundu.application.dtos.ResetPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequestDTO(
        @NotBlank(message = "Email não pode estar em branco")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Método de recuperação é obrigatório")
        String method
) {
}
