package ao.com.wundu.application.dtos;

public record TransactionResponseDTO(
        String id,
        Double amount,
        String description,
        String type,
        String creditCardId,
        String dateTime) {
}
