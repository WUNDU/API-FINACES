package ao.com.wundu.infrastructure.services;

public interface SmsService {
    void sendSms(String phone, String message);
}
