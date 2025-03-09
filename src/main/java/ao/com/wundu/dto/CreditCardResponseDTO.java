package ao.com.wundu.dto;

import ao.com.wundu.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditCardResponseDTO(
        String id,
        String cardNumber,
        String bankName,
        BigDecimal creditLimit,
        LocalDate expirationDate,
        String userId
) {
}
