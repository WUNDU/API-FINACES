package ao.com.wundu.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "TB_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idCategory;

    @Column(nullable = false)
    private String nameCategory;

    @Column(nullable = false)
    private String icon;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Category() {
    }

    public Category(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public String getIcon() {
        return icon;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void associateTransaction(Transaction transaction) {
        if (transaction != null) {
            transaction.setCategory(this);
            this.transactions.add(transaction);
        }
    }

    public double calculateTotalExpense() {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}
