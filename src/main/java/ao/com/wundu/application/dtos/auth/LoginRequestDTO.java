package ao.com.wundu.application.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email não pode estar em branco")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha não pode estar em branco")
        String password
) {
}
