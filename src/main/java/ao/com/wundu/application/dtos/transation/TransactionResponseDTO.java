package ao.com.wundu.application.dtos.transation;

import java.time.LocalDateTime;

public record TransactionResponseDTO(
        String id,
        Double amount,
        String description,
        String type,
        String creditCardId,
        LocalDateTime dateTime,
        String categoryId

) {
}
