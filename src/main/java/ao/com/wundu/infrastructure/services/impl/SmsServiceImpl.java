package ao.com.wundu.infrastructure.services.impl;

import ao.com.wundu.infrastructure.services.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Override
    public void sendSms(String phone, String message) {
        // Implementação fictícia; substituir por integração real (ex.: Twilio)
        System.out.println("Enviando SMS para " + phone + ": " + message);
    }
}
