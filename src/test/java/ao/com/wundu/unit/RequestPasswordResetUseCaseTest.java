package ao.com.wundu.unit;

import ao.com.wundu.application.dtos.ResetPassword.PasswordResetRequestDTO;
import ao.com.wundu.application.usercases.auth.RequestPasswordResetUseCase;
import ao.com.wundu.domain.entities.PasswordResetToken;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.domain.exceptions.InvalidCredentialsException;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.PasswordResetTokenRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.EmailService;
import ao.com.wundu.infrastructure.services.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.ServiceUnavailableException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestPasswordResetUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @InjectMocks
    private RequestPasswordResetUseCase useCase;

    private PasswordResetRequestDTO emailDto;
    private PasswordResetRequestDTO smsDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("john.doe@example.com");
        user.setPhone("923456789");

        emailDto = new PasswordResetRequestDTO("john.doe@example.com", "email");
        smsDto = new PasswordResetRequestDTO("john.doe@example.com", "sms");
    }

    @Test
    void shouldRequestPasswordResetViaEmailSuccessfully() throws ServiceUnavailableException {
        when(userRepository.findByEmail(emailDto.email())).thenReturn(Optional.of(user));
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(new PasswordResetToken());
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        useCase.execute(emailDto);

        verify(userRepository).findByEmail(emailDto.email());
        verify(tokenRepository).save(any(PasswordResetToken.class));
        verify(emailService).sendEmail(eq("john.doe@example.com"), eq("Recuperação de Senha"), anyString());
        verify(smsService, never()).sendSms(anyString(), anyString());
    }

    @Test
    void shouldRequestPasswordResetViaSmsSuccessfully() throws ServiceUnavailableException {
        when(userRepository.findByEmail(smsDto.email())).thenReturn(Optional.of(user));
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(new PasswordResetToken());
        doNothing().when(smsService).sendSms(anyString(), anyString());

        useCase.execute(smsDto);

        verify(userRepository).findByEmail(smsDto.email());
        verify(tokenRepository).save(any(PasswordResetToken.class));
        verify(smsService).sendSms(eq("+244923456789"), anyString());
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }


    @Test
    void shouldThrowUserNotFoundException() {
        when(userRepository.findByEmail(emailDto.email())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(emailDto));
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionForInvalidMethod() {
        PasswordResetRequestDTO invalidDto = new PasswordResetRequestDTO("john.doe@example.com", "invalid");

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(invalidDto));

        verifyNoInteractions(userRepository, tokenRepository, smsService, emailService);
    }

    @Test
    void shouldThrowUserNotFoundExceptionForUnknownEmail() {
        when(userRepository.findByEmail(smsDto.email())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(smsDto));

        verify(userRepository).findByEmail(smsDto.email());
        verifyNoInteractions(tokenRepository, smsService, emailService);
    }
}
