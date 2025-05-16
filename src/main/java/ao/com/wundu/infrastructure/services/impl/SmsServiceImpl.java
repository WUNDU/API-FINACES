package ao.com.wundu.infrastructure.services.impl;

import ao.com.wundu.domain.exceptions.SmsSendingException;
import ao.com.wundu.infrastructure.services.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Value("${sms.mock.enabled:true}")
    private boolean mockEnabled;

    @Override
    public void sendSms(String phone, String message) {
        if (!StringUtils.hasText(phone)) {
            logger.error("Número de telefone é inválido: {}", phone);
            throw new IllegalArgumentException("Número de telefone é obrigatório");
        }
        if (!StringUtils.hasText(message)) {
            logger.error("Mensagem do SMS é inválida");
            throw new IllegalArgumentException("Mensagem do SMS é obrigatória");
        }
        if (phone.length() < 9 || !phone.matches("\\+?\\d+")) {
            logger.error("Formato de telefone inválido: {}", phone);
            throw new IllegalArgumentException("Formato de telefone inválido");
        }
        if (message.length() > 160) {
            logger.error("Mensagem excede o limite de 160 caracteres: {}", message.length());
            throw new IllegalArgumentException("Mensagem deve ter no máximo 160 caracteres");
        }

        logger.info("Enviando SMS para: {}, Mensagem: {}", phone, message);
        try {
            if (mockEnabled) {
                // Simulação de envio com possibilidade de falha
                if (phone.contains("999999999")) {
                    throw new SmsSendingException("Simulação de falha no envio de SMS");
                }
                logger.info("SMS simulado enviado para: {}", phone);
            } else {
                // TODO: Implementar integração real (ex.: Twilio)
                logger.warn("Integração real de SMS não implementada");
                throw new UnsupportedOperationException("Integração de SMS não configurada");
            }
        } catch (Exception e) {
            logger.error("Falha ao enviar SMS para: {}", phone, e);
            throw new SmsSendingException("Falha ao enviar SMS");
        }
    }
}
