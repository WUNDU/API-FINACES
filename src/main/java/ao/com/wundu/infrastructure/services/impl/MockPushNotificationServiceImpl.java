package ao.com.wundu.infrastructure.services.impl;

import ao.com.wundu.domain.exceptions.PushNotificationException;
import ao.com.wundu.infrastructure.services.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MockPushNotificationServiceImpl implements PushNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(MockPushNotificationServiceImpl.class);

    @Override
    public void sendPushNotification(String userId, String message) {

        if (!StringUtils.hasText(userId)) {
            logger.error("ID do usuário é inválido: {}", userId);
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }
        if (!StringUtils.hasText(message)) {
            logger.error("Mensagem do push é inválida");
            throw new IllegalArgumentException("Mensagem do push é obrigatória");
        }

        logger.info("Simulando envio de push para usuário: {}, Mensagem: {}", userId, message);
        try {
            // Simulação de envio com possibilidade de falha
            if (userId.contains("error")) {
                throw new PushNotificationException("Simulação de falha no envio de push");
            }
            logger.info("Push simulado enviado para usuário: {}", userId);
        } catch (Exception e) {
            logger.error("Falha ao enviar push para usuário: {}", userId, e);
            throw new PushNotificationException("Falha ao enviar push");
        }

    }
}
