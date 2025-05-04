package ao.com.wundu.infrastructure.repositories;

import ao.com.wundu.domain.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {

    List<CreditCard> findByUserId(String userId);
}
