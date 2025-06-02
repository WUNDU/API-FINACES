package ao.com.wundu.application.dtos.transation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record TransactionEventDTO(
        @NotBlank(message = "externalCardId é obrigatório")
        String externalCardId,

        @NotNull(message = "amount é obrigatório")
        @Positive(message = "amount deve ser positivo")
        Double amount,

        @NotBlank(message = "type é obrigatório")
        String type,

        @NotNull(message = "dateTime é obrigatório")
        LocalDateTime dateTime,

        @NotBlank(message = "description é obrigatória")
        String description
) {
}
