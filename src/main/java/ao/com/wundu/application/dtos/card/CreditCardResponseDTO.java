package ao.com.wundu.application.dtos.card;

import java.math.BigDecimal;

public record CreditCardResponseDTO(
        String id,
        String maskedCardNumber,
        String bankName,
        String expirationDate,
        String userId
) {
}
