package ao.com.wundu.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double amount;

    private String description;

    private String type;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "creditcard_id")
    private CreditCard creditCard;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Transaction() {
    }

    public Transaction(String id, Double amount, String description, String type, LocalDateTime dateTime,
                       CreditCard creditCard) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.creditCard = creditCard;
        this.dateTime = dateTime;
    }

    public Transaction(Double amount, String description, String type, CreditCard creditCard) {
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.creditCard = creditCard;
        this.dateTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
