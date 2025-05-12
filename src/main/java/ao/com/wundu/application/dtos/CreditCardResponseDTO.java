package ao.com.wundu.application.dtos;

//import jakarta.persistence.*;

import java.math.BigDecimal;

public record CreditCardResponseDTO(
        String id,
        String cardNumber,
        String bankName,
        BigDecimal creditLimit,
        String expirationDate,
        String userId
) {
}
