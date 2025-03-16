package ao.com.wundu.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TB_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idCategory;

    @Column(nullable = false)
    private String nameCategory;

    @Column(nullable = false)
    private String description;

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL)
    @JoinColumn(name = "id_transaction")
    private Transaction transaction;

    public Category() {
    }

    public Category(String nameCategory, String description, Transaction transaction) {
        this.nameCategory = nameCategory;
        this.description = description;
        this.transaction = transaction;
    }

    // Getters e Setters

    public String getIdCategory() {
        return idCategory;
    }

    public String getDescription() {
        return description;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
