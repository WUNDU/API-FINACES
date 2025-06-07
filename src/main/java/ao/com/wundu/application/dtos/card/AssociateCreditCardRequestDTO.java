package ao.com.wundu.application.dtos.card;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record AssociateCreditCardRequestDTO(

        @NotBlank(message = "ID do usuário é obrigatório")
        String userId,

        String bankName,

        @NotBlank(message = "Número do cartão é obrigatório")
        @Pattern(regexp = "\\d{16}", message = "Número do cartão deve ter 16 dígitos")
        String cardNumber,

        @NotNull(message = "Data de expiração é obrigatória")
        @Future(message = "Data de expiração deve ser futura")
        LocalDate expirationDate
) {
}
