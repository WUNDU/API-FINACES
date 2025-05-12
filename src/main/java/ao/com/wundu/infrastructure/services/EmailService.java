package ao.com.wundu.infrastructure.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
