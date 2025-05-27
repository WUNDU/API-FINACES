package ao.com.wundu.application.dtos.card;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditCardCreateDTO(

        @NotBlank(message = "Número do cartão é obrigatório")
        @Pattern(regexp = "\\d{16}", message = "Número do cartão deve ter 16 dígitos")
        String cardNumber,

        @NotBlank(message = "Nome do banco é obrigatório")
        String bankName,

        @NotNull(message = "Limite de crédito é obrigatório")
        @DecimalMin(value = "0.01", message = "Limite de crédito deve ser maior que zero")
        BigDecimal creditLimit,

        @NotNull(message = "Data de expiração é obrigatória")
        @Future(message = "Data de expiração deve ser futura")
        LocalDate expirationDate,

        @NotBlank(message = "Plaid Access Token é obrigatório")
        String plaidAccessToken,

        @NotBlank(message = "Plaid Item ID é obrigatório")
        String plaidItemId

) {
}