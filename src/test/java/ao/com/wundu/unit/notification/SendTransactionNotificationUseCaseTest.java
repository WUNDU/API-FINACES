package ao.com.wundu.unit.notification;

import ao.com.wundu.application.usercases.notification.SendTransactionNotificationUseCase;
import ao.com.wundu.domain.entities.Notification;
import ao.com.wundu.domain.entities.Transaction;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.domain.exceptions.EmailSendingException;
import ao.com.wundu.domain.exceptions.SmsSendingException;
import ao.com.wundu.domain.exceptions.TransactionNotFoundException;
import ao.com.wundu.infrastructure.repositories.NotificationRepository;
import ao.com.wundu.infrastructure.repositories.TransactionRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.EmailService;
import ao.com.wundu.infrastructure.services.PushNotificationService;
import ao.com.wundu.infrastructure.services.SmsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendTransactionNotificationUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @Mock
    private PushNotificationService pushNotificationService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private SendTransactionNotificationUseCase useCase;

    @Test
    void shouldSendEmailNotificationSuccessfully() {
        String userId = "user-1";
        String transactionId = "tx-1";
        Double amount = 100.0;
        LocalDateTime transactionDate = LocalDateTime.now();
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setNotificationPreference(NotificationPreference.EMAIL);
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        Notification pendingNotification = new Notification(user, transaction, NotificationPreference.EMAIL, "Transação de 100.0 realizada.", "PENDING");
        Notification sentNotification = new Notification(user, transaction, NotificationPreference.EMAIL, "Transação de 100.0 realizada.", "SENT");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(messageSource.getMessage(eq("notification.transaction"), any(), any())).thenReturn("Transação de 100.0 realizada.");
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> {
                    Notification saved = invocation.getArgument(0);
                    Notification copy = new Notification(saved.getUser(), saved.getTransaction(), saved.getChannel(), saved.getMessage(), saved.getStatus());
                    copy.setSentAt(saved.getSentAt());
                    return copy;
                });

        useCase.execute(userId, transactionId, amount, transactionDate);

        verify(emailService).sendEmail(eq("test@example.com"), eq("Nova Transação"), anyString());
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verifyNoInteractions(smsService, pushNotificationService);
    }

    @Test
    void shouldHandleEmailSendingException() {
        String userId = "user-1";
        String transactionId = "tx-1";
        Double amount = 100.0;
        LocalDateTime transactionDate = LocalDateTime.now();
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setNotificationPreference(NotificationPreference.EMAIL);
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        Notification pendingNotification = new Notification(user, transaction, NotificationPreference.EMAIL, "Transação de 100.0 realizada.", "PENDING");
        Notification failedNotification = new Notification(user, transaction, NotificationPreference.EMAIL, "Transação de 100.0 realizada.", "FAILED");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(messageSource.getMessage(eq("notification.transaction"), any(), any())).thenReturn("Transação de 100.0 realizada.");
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> {
                    Notification saved = invocation.getArgument(0);
                    Notification copy = new Notification(saved.getUser(), saved.getTransaction(), saved.getChannel(), saved.getMessage(), saved.getStatus());
                    return copy;
                });
        doThrow(new EmailSendingException("Erro de envio")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        assertThrows(EmailSendingException.class, () -> useCase.execute(userId, transactionId, amount, transactionDate));
        verify(emailService).sendEmail(eq("test@example.com"), eq("Nova Transação"), anyString());
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verifyNoInteractions(smsService, pushNotificationService);
    }

    @Test
    void shouldSendSmsNotificationSuccessfully() {
        String userId = "user-1";
        String transactionId = "tx-1";
        Double amount = 100.0;
        LocalDateTime transactionDate = LocalDateTime.now();
        User user = new User();
        user.setId(userId);
        user.setPhone("+244923456789");
        user.setNotificationPreference(NotificationPreference.SMS);
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        Notification pendingNotification = new Notification(user, transaction, NotificationPreference.SMS, "Transação de 100.0 realizada.", "PENDING");
        Notification sentNotification = new Notification(user, transaction, NotificationPreference.SMS, "Transação de 100.0 realizada.", "SENT");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(messageSource.getMessage(eq("notification.transaction"), any(), any())).thenReturn("Transação de 100.0 realizada.");
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> {
                    Notification saved = invocation.getArgument(0);
                    Notification copy = new Notification(saved.getUser(), saved.getTransaction(), saved.getChannel(), saved.getMessage(), saved.getStatus());
                    copy.setSentAt(saved.getSentAt());
                    return copy;
                });

        useCase.execute(userId, transactionId, amount, transactionDate);

        verify(smsService).sendSms(eq("+244923456789"), anyString());
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verifyNoInteractions(emailService, pushNotificationService);
    }

    @Test
    void shouldHandleSmsSendingException() {
        String userId = "user-1";
        String transactionId = "tx-1";
        Double amount = 100.0;
        LocalDateTime transactionDate = LocalDateTime.now();
        User user = new User();
        user.setId(userId);
        user.setPhone("+244999999999");
        user.setNotificationPreference(NotificationPreference.SMS);
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        Notification pendingNotification = new Notification(user, transaction, NotificationPreference.SMS, "Transação de 100.0 realizada.", "PENDING");
        Notification failedNotification = new Notification(user, transaction, NotificationPreference.SMS, "Transação de 100.0 realizada.", "FAILED");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(messageSource.getMessage(eq("notification.transaction"), any(), any())).thenReturn("Transação de 100.0 realizada.");
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> {
                    Notification saved = invocation.getArgument(0);
                    Notification copy = new Notification(saved.getUser(), saved.getTransaction(), saved.getChannel(), saved.getMessage(), saved.getStatus());
                    return copy;
                });
        doThrow(new SmsSendingException("Erro de envio")).when(smsService).sendSms(anyString(), anyString());

        assertThrows(SmsSendingException.class, () -> useCase.execute(userId, transactionId, amount, transactionDate));
        verify(smsService).sendSms(eq("+244999999999"), anyString());
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verifyNoInteractions(emailService, pushNotificationService);
    }
}
