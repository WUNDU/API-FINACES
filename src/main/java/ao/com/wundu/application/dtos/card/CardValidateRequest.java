package ao.com.wundu.application.dtos.card;

import java.time.LocalDate;

public record CardValidateRequest(
        String cardNumber,
        String bankName,
        LocalDate expirationDate
) {
}
