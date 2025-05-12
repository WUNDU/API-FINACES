package ao.com.wundu.application.dtos.ResetPassword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordResetConfirmDTO(
        @NotBlank(message = "Token não pode estar em branco")
        String token,

        @NotBlank(message = "Nova senha não pode estar em branco")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "Senha deve conter pelo menos uma letra maiúscula e um número")
        String newPassword
) {
}
