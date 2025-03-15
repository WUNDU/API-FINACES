package ao.com.wundu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "E-mail é obrigátorio")
        @Email(message = "E-mail é inválido")
        String email,

        @NotBlank(message = "Password é obrigátorio")
        String password
) {
}
