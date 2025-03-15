package ao.com.wundu.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetConfirmDTO(

        @NotBlank(message = "Token é obrigatório")
        String token,

        @NotBlank(message = "Nova senha é obrigatório")
        String newPassWord

) {
}
