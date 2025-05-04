package ao.com.wundu.infrastructure.repositories;

import ao.com.wundu.domain.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {}
