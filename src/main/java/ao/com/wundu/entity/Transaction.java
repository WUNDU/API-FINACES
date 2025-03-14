package ao.com.wundu.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Double amount;

    private String description;

    private String type;

    private String accountId;
    
    private LocalDateTime dateTime;

    public Transaction() {}

    public Transaction(String id, Double amount, String description, String type, LocalDateTime dateTime, String accountId) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.dateTime = dateTime;
        this.accountId = accountId;
    }

    public Transaction(Double amount, String description, String type, String accountId) {
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.accountId = accountId;
        this.dateTime = LocalDateTime.now();
    }

    public String getId() { return id; }
    public Double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getAccountId() { return accountId; }
    public LocalDateTime getDateTime() { return dateTime; }

    public void setAmount(Double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
}
