package ao.com.wundu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "Nome não pode estar em branco")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 a 100 caracteres")
        String name,

        @NotBlank(message = "Email não pode estra em branco")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Password não pode estar em branco")
        @Size(min = 8, message = "Password deve ter no mínimo 8 caracteres")
        String password
) {
}
