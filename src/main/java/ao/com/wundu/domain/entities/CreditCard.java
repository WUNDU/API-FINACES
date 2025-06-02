package ao.com.wundu.domain.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "credit_cards", indexes = {
        @Index(name = "idx_credit_card_number", columnList = "card_number", unique = true)
})
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "external_card_id", unique = true, nullable = false)
    private String externalCardId; // Novo campo para cardId da Wundu Banking API

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber; // Armazena número criptografado

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CreditCard() {
    }

    public CreditCard(String cardNumber, String bankName, LocalDate expirationDate, User user) {
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternalCardId() {
        return externalCardId;
    }

    public void setExternalCardId(String externalCardId) {
        this.externalCardId = externalCardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getMaskedCardNumber() {
        // Exibe apenas os últimos 4 dígitos (ex.: **** **** **** 1234)
        if (cardNumber == null || cardNumber.length() < 4) {
            return "**** **** **** ****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFormattedExpirationDate() {
        if (expirationDate == null) {
            return null;
        }
        return expirationDate.format(DateTimeFormatter.ofPattern("MM/yy"));
    }

    public static boolean validateCardNumber(String cardNumber) {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            return false;
        }
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public static boolean validateExpirationDate(LocalDate expirationDate) {
        if (expirationDate == null) {
            return false;
        }
        return expirationDate.isAfter(LocalDate.now());
    }
}
