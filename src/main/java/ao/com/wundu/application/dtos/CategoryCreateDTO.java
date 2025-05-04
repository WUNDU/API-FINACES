package ao.com.wundu.application.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDTO(
    @NotBlank(message = "O nome da categoria não pode estar em branco")
    String nameCategory,
    String description) {
}
