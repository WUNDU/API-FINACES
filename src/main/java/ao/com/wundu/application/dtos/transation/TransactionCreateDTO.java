package ao.com.wundu.application.dtos.transation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionCreateDTO(
        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        Double amount,
        String description,
        @NotBlank(message = "Tipo é obrigatório")
        String type

) {
}
