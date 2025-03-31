package ao.com.wundu.dto;

import ao.com.wundu.entity.CreditCard;
import ao.com.wundu.entity.User;
//import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record CreditCardResponseDTO(
        String id,
        String cardNumber,
        String bankName,
        BigDecimal creditLimit,
        String expirationDate,
        String userId
) {

    // Método de fábrica para converter LocalDate para String
    public static CreditCardResponseDTO fromEntity(CreditCard creditCard) {
        return new CreditCardResponseDTO(
                creditCard.getId(),
                creditCard.getCardNumber(),
                creditCard.getBankName(),
                creditCard.getCreditLimit(),
                formatExpirationDate(creditCard.getExpirationDate()), // Formata a data
                creditCard.getUser().getId()
        );
    }

    // Método auxiliar para formatar a data
    private static String formatExpirationDate(LocalDate expirationDate) {
        return (expirationDate != null) ? expirationDate.format(DateTimeFormatter.ofPattern("MM/yy")) : null;
    }

}
