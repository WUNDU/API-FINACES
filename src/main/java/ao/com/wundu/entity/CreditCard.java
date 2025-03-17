package ao.com.wundu.entity;


//import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

//@Entity
//@Table(name = "TB_cards")
@Document(collection = "cards")
public class CreditCard {

    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(name = "id_card")
    private String id;

//    @Column( name = "card_number", unique = true, nullable = false )
    private String cardNumber;

//    @Column( name = "bank_name", nullable = false )
    private String bankName;

//    @Column( name = "credit_limit", nullable = false )
    private BigDecimal creditLimit;

//    @Column( name = "expitation_date", nullable = false )
    private LocalDate expirationDate;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
    @DBRef
    private User user;

    public CreditCard() {
    }

    public CreditCard(String cardNumber, String bankName, BigDecimal creditLimit, LocalDate expirationDate, User user) {
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.creditLimit = creditLimit;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
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
}
