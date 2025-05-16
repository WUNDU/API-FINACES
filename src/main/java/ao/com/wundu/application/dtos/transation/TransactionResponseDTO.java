package ao.com.wundu.application.dtos.transation;

public record TransactionResponseDTO(
        String id,
        Double amount,
        String description,
        String type,
        String creditCardId,
        String dateTime,
        String categoryId

) {
}
