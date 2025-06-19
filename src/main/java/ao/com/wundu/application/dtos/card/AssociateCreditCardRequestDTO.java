package ao.com.wundu.application.dtos.card;

import ao.com.wundu.application.validators.FutureMMYY;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record AssociateCreditCardRequestDTO(

        @NotBlank(message = "ID do usuário é obrigatório")
        String userId,

        //String bankName,

        @NotBlank(message = "Número do cartão é obrigatório")
        @Pattern(regexp = "\\d{16}", message = "Número do cartão deve ter 16 dígitos")
        String cardNumber,

        @NotNull(message = "Data de expiração é obrigatória")
//        @Pattern(regexp = "\\d{2}/\\d{2}", message = "Data de expiração deve estar no formato MM/yy")
//        @FutureMMYY(message = "Data de expiração deve ser futura") // Agora
        @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "Data de expiração deve estar no formato MM/yy (exemplo: 02/27)")
        String expirationDate,

        @NotBlank(message = "Nome no cartão é obrigatório")
        @Size(min = 2, max = 50, message = "Nome no cartão deve ter entre 2 e 50 caracteres")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Nome do portador deve conter apenas letras e espaços")
        String cardHolderName
) {
}
