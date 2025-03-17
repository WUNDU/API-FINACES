package ao.com.wundu.repository;

import ao.com.wundu.entity.CreditCard;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CreditCardRepository extends MongoRepository<CreditCard, String> {

    List<CreditCard> findByUserId(String userId);
}
