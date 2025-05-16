package ao.com.wundu.application.usercases.notification;

import ao.com.wundu.domain.entities.Notification;
import ao.com.wundu.domain.entities.Transaction;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.domain.exceptions.*;
import ao.com.wundu.infrastructure.repositories.NotificationRepository;
import ao.com.wundu.infrastructure.repositories.TransactionRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.EmailService;
import ao.com.wundu.infrastructure.services.PushNotificationService;
import ao.com.wundu.infrastructure.services.SmsService;
import ao.com.wundu.infrastructure.services.impl.EmailServiceImpl;
import ao.com.wundu.infrastructure.services.impl.SmsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class SendTransactionNotificationUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SendTransactionNotificationUseCase.class);

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SmsService smsService;
    private final PushNotificationService pushNotificationService;
    private final MessageSource messageSource;

    public SendTransactionNotificationUseCase(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            NotificationRepository notificationRepository,
            EmailService emailService,
            SmsService smsService,
            PushNotificationService pushNotificationService,
            MessageSource messageSource
    ) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
        this.smsService = smsService;
        this.pushNotificationService = pushNotificationService;
        this.messageSource = messageSource;
    }

    public void execute(String userId, String transactionId, Double amount, LocalDateTime transactionDate) {
        logger.info("Iniciando envio de notificação para usuário: {}, transação: {}", userId, transactionId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado: {}", userId);
                    return new UserNotFoundException("Usuário não encontrado");
                });

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    logger.error("Transação não encontrada: {}", transactionId);
                    return new TransactionNotFoundException("Transação não encontrada");
                });

        NotificationPreference preference = user.getNotificationPreference();
        if (preference == null) {
            logger.warn("Nenhuma preferência definida para usuário: {}. Usando padrão: SMS", userId);
            preference = NotificationPreference.SMS;
        }

        // Validar disponibilidade do canal
        if (preference == NotificationPreference.SMS && (user.getPhone() == null || user.getPhone().isEmpty())) {
            logger.error("SMS não disponível para usuário: {} (telefone não registrado)", userId);
            throw new IllegalStateException("Telefone não registrado para notificações SMS");
        }
        if (preference == NotificationPreference.EMAIL && (user.getEmail() == null || user.getEmail().isEmpty())) {
            logger.error("E-mail não disponível para usuário: {}", userId);
            throw new IllegalStateException("E-mail não registrado para notificações");
        }

        // Criar mensagem com i18n
        String message = messageSource.getMessage(
                "notification.transaction",
                new Object[]{amount, transactionDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))},
                Locale.forLanguageTag("pt-PT")
        );

        // Criar notificação
        Notification notification = new Notification(user, transaction, preference, message, "PENDING");
        notificationRepository.save(notification);

        try {
            switch (preference) {
                case EMAIL:
                    emailService.sendEmail(user.getEmail(), "Nova Transação", message);
                    break;
                case SMS:
                    smsService.sendSms(user.getPhone(), message);
                    break;
                case PUSH:
                    pushNotificationService.sendPushNotification(user.getId(), message);
                    break;
                default:
                    logger.error("Canal de notificação inválido: {}", preference);
                    throw new IllegalStateException("Canal de notificação inválido");
            }
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());
            logger.info("Notificação enviada com sucesso para usuário: {}, canal: {}", userId, preference);
        } catch (EmailSendingException | SmsSendingException | PushNotificationException e) {
            notification.setStatus("FAILED");
            logger.error("Falha ao enviar notificação para usuário: {}, canal: {}", userId, preference, e);
            throw e;
        } finally {
            notificationRepository.save(notification);
        }
    }
}
