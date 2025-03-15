package ao.com.wundu.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.phone.number}")
    private String fromNumber;

    public void sendSms(String to, String message) {
        // Simulação (em produção, usar Twilio)
        System.out.println("SMS para " + to + ": " + message);
    }
}
