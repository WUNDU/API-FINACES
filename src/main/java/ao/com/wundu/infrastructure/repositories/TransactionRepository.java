package ao.com.wundu.infrastructure.repositories;

import ao.com.wundu.application.dtos.transation.TransactionResponseDTO;
import ao.com.wundu.domain.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<TransactionResponseDTO> findByCreditCardId(String creditCardId);
    List<Transaction> findByCategoryId(String categoryId);
    Optional<Transaction> findByCreditCardIdAndAmountAndDescriptionAndTypeAndDateTime(
            String creditCardId,
            Double amount,
            String description,
            String type,
            LocalDateTime dateTime
    );
}
