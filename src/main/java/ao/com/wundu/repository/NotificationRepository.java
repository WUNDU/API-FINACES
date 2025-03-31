package ao.com.wundu.repository;

import ao.com.wundu.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {}
