package ao.com.wundu.infrastructure.services;

public interface PushNotificationService {

    void sendPushNotification(String userId, String message);
}
