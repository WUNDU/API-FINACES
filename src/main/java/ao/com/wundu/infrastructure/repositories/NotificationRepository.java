package ao.com.wundu.infrastructure.repositories;

import ao.com.wundu.domain.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
    List<Notification> findByTransactionId(String transactionId);
}
