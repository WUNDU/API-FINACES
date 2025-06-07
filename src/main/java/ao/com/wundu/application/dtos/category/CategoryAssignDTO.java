package ao.com.wundu.application.dtos.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryAssignDTO(

        @NotBlank(message = "ID da categoria é obrigatório")
        String categoryId
) {
}
