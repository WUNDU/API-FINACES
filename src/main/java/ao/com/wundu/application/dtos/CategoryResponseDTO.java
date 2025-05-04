package ao.com.wundu.application.dtos;

public record CategoryResponseDTO(
        String idCategory,
        String nameCategory,
        String description,
        String transactionId) {
}
