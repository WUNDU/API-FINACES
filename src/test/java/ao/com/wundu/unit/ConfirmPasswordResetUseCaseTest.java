package ao.com.wundu.unit;

import ao.com.wundu.application.dtos.ResetPassword.PasswordResetConfirmDTO;
import ao.com.wundu.application.usercases.auth.ConfirmPasswordResetUseCase;
import ao.com.wundu.domain.entities.PasswordResetToken;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.domain.exceptions.InvalidTokenException;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.PasswordResetTokenRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfirmPasswordResetUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ConfirmPasswordResetUseCase useCase;

    private PasswordResetConfirmDTO dto;
    private User user;
    private PasswordResetToken token;

    @BeforeEach
    void setUp() {
        dto = new PasswordResetConfirmDTO("token123", "NewPassword123");
        user = new User(
                "John Doe",
                "john.doe@example.com",
                "encoded",
                "+244923456789",
                NotificationPreference.EMAIL
        );
        token = new PasswordResetToken(
                "token123",
                user,
                LocalDateTime.now().plusMinutes(10)
        );
    }

    @Test
    void shouldConfirmPasswordResetSuccessfully() {
        when(tokenRepository.findByToken(dto.token())).thenReturn(Optional.of(token));
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        when(passwordEncoder.encode(dto.newPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        useCase.execute(dto);

        verify(userRepository).save(user);
        verify(tokenRepository).delete(token);
        assertEquals("encoded", user.getPassword());
    }

    @Test
    void shouldThrowInvalidTokenExceptionForNonExistentToken() {
        when(tokenRepository.findByToken(dto.token())).thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class, () -> useCase.execute(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidTokenExceptionForExpiredToken() {
        token.setExpiryDate(LocalDateTime.now().minusMinutes(10));
        when(tokenRepository.findByToken(dto.token())).thenReturn(Optional.of(token));

        assertThrows(InvalidTokenException.class, () -> useCase.execute(dto));
        verify(tokenRepository).delete(token);
    }

    @Test
    void shouldThrowUserNotFoundException() {
        when(tokenRepository.findByToken(dto.token())).thenReturn(Optional.of(token));
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> useCase.execute(dto));
        verify(userRepository, never()).save(any());
    }
}
