package ao.com.wundu.infrastructure.services.impl;

import ao.com.wundu.domain.exceptions.EmailSendingException;
import ao.com.wundu.infrastructure.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        if (!StringUtils.hasText(to)) {
            logger.error("Destinatário do e-mail é inválido: {}", to);
            throw new IllegalArgumentException("Destinatário do e-mail é obrigatório");
        }
        if (!StringUtils.hasText(subject)) {
            logger.error("Assunto do e-mail é inválido");
            throw new IllegalArgumentException("Assunto do e-mail é obrigatório");
        }
        if (!StringUtils.hasText(body)) {
            logger.error("Corpo do e-mail é inválido");
            throw new IllegalArgumentException("Corpo do e-mail é obrigatório");
        }

        logger.info("Enviando e-mail para: {}, Assunto: {}", to, subject);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            logger.info("E-mail enviado com sucesso para: {}", to);
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail para: {}", to, e);
            throw new EmailSendingException("Falha ao enviar e-mail");
        }
    }
}
