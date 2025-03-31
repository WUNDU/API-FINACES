package ao.com.wundu.repository;

import ao.com.wundu.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {

    List<CreditCard> findByUserId(String userId);
}
