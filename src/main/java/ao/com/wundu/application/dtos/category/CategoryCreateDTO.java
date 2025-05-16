package ao.com.wundu.application.dtos.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDTO(
        @NotBlank(message = "Nome da categoria é obrigatório")
        String name,
        @NotBlank(message = "Descrição é obrigatória")
        String description
){
}
