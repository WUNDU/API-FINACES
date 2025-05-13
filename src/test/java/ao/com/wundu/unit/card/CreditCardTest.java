package ao.com.wundu.unit.card;

import ao.com.wundu.domain.entities.CreditCard;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CreditCardTest {

    @Test
    void shouldValidateCardNumberWithLuhnAlgorithm() {
        assertTrue(CreditCard.validateCardNumber("4532015112830366")); // Válido
        assertFalse(CreditCard.validateCardNumber("4532015112830367")); // Inválido
        assertFalse(CreditCard.validateCardNumber("123456789012345")); // Menos de 16 dígitos
        assertFalse(CreditCard.validateCardNumber("12345678901234567")); // Mais de 16 dígitos
        assertFalse(CreditCard.validateCardNumber(null)); // Nulo
        assertFalse(CreditCard.validateCardNumber("abcdefghijklmnop")); // Não numérico
    }

    @Test
    void shouldValidateExpirationDate() {
        assertTrue(CreditCard.validateExpirationDate(LocalDate.now().plusMonths(1))); // Futura
        assertFalse(CreditCard.validateExpirationDate(LocalDate.now().minusMonths(1))); // Passada
        assertFalse(CreditCard.validateExpirationDate(LocalDate.now())); // Hoje
        assertFalse(CreditCard.validateExpirationDate(null)); // Nulo
    }

    @Test
    void shouldMaskCardNumberCorrectly() {
        CreditCard card = new CreditCard();
        card.setCardNumber("4532015112830366");
        assertEquals("**** **** **** 0366", card.getMaskedCardNumber());

        card.setCardNumber("123");
        assertEquals("**** **** **** ****", card.getMaskedCardNumber());

        card.setCardNumber(null);
        assertEquals("**** **** **** ****", card.getMaskedCardNumber());
    }

    @Test
    void shouldFormatExpirationDate() {
        CreditCard card = new CreditCard();
        card.setExpirationDate(LocalDate.of(2025, 12, 1));
        assertEquals("12/25", card.getFormattedExpirationDate());
    }
}
