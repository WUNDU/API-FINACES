package ao.com.wundu.application.dtos.transation;

import jakarta.validation.constraints.NotBlank;

public record CategorizeTransactionRequestDTO(
        @NotBlank(message = "ID da categoria é obrigatório")
        String categoryId
) {
}
