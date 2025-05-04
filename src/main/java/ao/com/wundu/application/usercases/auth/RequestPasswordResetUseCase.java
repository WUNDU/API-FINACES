package ao.com.wundu.application.usercases.auth;

import ao.com.wundu.application.dtos.ResetPassword.PasswordResetRequestDTO;
import ao.com.wundu.domain.entities.PasswordResetToken;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.exceptions.InvalidCredentialsException;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.PasswordResetTokenRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.EmailService;
import ao.com.wundu.infrastructure.services.SmsService;
import ao.com.wundu.infrastructure.services.impl.EmailServiceImpl;
import ao.com.wundu.infrastructure.services.impl.SmsServiceImpl;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class RequestPasswordResetUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RequestPasswordResetUseCase.class);
    private static final int TOKEN_EXPIRY_MINUTES = 30;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public void execute(PasswordResetRequestDTO dto) throws ServiceUnavailableException {

        logger.info("Iniciando solicitação de redefinição de senha para email: {}, método: {}", dto.email(), dto.method());
        if (dto.method() == null || (!dto.method().equals("email") && !dto.method().equals("sms"))) {
            logger.error("Método de recuperação inválido: {}", dto.method());
            throw new InvalidCredentialsException("Método de recuperação deve ser 'email' ou 'sms'");
        }

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_MINUTES));

        try {
            tokenRepository.save(resetToken);
            logger.info("Token de redefinição salvo para usuário: {}", dto.email());
        } catch (Exception e) {
            logger.error("Erro ao salvar token de redefinição para usuário: {}", dto.email(), e);
            throw new ServiceUnavailableException("Erro ao salvar token de redefinição");
        }

        String message = String.format("Seu código de recuperação é: %s. Válido por %d minutos.", token, TOKEN_EXPIRY_MINUTES);

        if ("email".equals(dto.method())) {
            sendEmail(user.getEmail(), message);
        } else {
            sendSms(user.getPhone(), message);
        }
    }

    @Retry(name = "emailRetry")
    private void sendEmail(String email, String message) throws ServiceUnavailableException {
        try {
            if (emailService == null) {
                logger.error("emailService não inicializado");
                throw new IllegalStateException("Serviço de e-mail não configurado");
            }
            emailService.sendEmail(email, "Recuperação de Senha", message);
            logger.info("E-mail de recuperação enviado para: {}", email);
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail de recuperação para {}", email, e);
            throw new ServiceUnavailableException("Falha ao enviar e-mail de recuperação");
        }
    }

    @Retry(name = "smsRetry")
    private void sendSms(String phone, String message) throws ServiceUnavailableException {
        try {
            if (smsService == null) {
                logger.error("smsService não inicializado");
                throw new IllegalStateException("Serviço de SMS não configurado");
            }
            smsService.sendSms("+244" + phone, message);
            logger.info("SMS de recuperação enviado para: {}", phone);
        } catch (Exception e) {
            logger.error("Falha ao enviar SMS de recuperação para {}", phone, e);
            throw new ServiceUnavailableException("Falha ao enviar SMS de recuperação");
        }
    }
}
