package ao.com.wundu.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditCardCreateDTO(

        @NotBlank(message = "Número do cartão é obrigatório")
        @Pattern(
                regexp = "\\*\\*\\*\\*-\\*\\*\\*\\*-\\*\\*\\*\\*-\\d{4}",
                message = "Formato do cartão deve ser ****-****-****-1234"
        )
        String cardNumber,

        @NotBlank(message = "Nome do banco é obrigatório")
        String bankName,

        @NotNull(message = "Limite de crédito é obrigatório")
        @DecimalMin(value = "0.01", message = "Limite de crédito deve ser maior que zero")
        BigDecimal creaditLimit,

        @NotNull(message = "Data de expiração é obrigatória")
        @Future(message = "Data de experiração deve ser futura")
        LocalDate expirationDate

) {
}
