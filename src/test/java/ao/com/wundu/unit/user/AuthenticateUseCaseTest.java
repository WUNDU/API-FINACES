package ao.com.wundu.unit.user;

import ao.com.wundu.application.dtos.auth.LoginRequestDTO;
import ao.com.wundu.application.dtos.auth.LoginResponseDTO;
import ao.com.wundu.application.usercases.auth.AuthenticateUseCase;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.domain.exceptions.InvalidCredentialsException;
import ao.com.wundu.domain.exceptions.LockedUserException;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.security.JwtUtil;
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
public class AuthenticateUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticateUseCase useCase;

    private LoginRequestDTO dto;
    private User user;

    @BeforeEach
    void setUp() {
        dto = new LoginRequestDTO("john.doe@example.com", "Password123");
        user = new User(
                "John Doe",
                "john.doe@example.com",
                "encoded",
                "+244923456789",
                NotificationPreference.EMAIL
        );
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("token");
        when(jwtUtil.generateRefreshToken(user.getEmail())).thenReturn("refreshToken");

        LoginResponseDTO result = useCase.authenticate(dto);

        assertNotNull(result);
        assertEquals("token", result.token());
        assertEquals("refreshToken", result.refreshToken());
        verify(userRepository).save(user);
        verify(userRepository).findByEmail(dto.email());
    }

    @Test
    void shouldThrowInvalidCredentialsException() {
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> useCase.authenticate(dto));
        verify(userRepository).save(user);
        assertEquals(1, user.getLoginAttempts());
    }

    @Test
    void shouldThrowLockedUserException() {
        user.setLocked(true);
        user.setLockedUntil(LocalDateTime.now().plusMinutes(10));
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));

        assertThrows(LockedUserException.class, () -> useCase.authenticate(dto));
        verify(userRepository, never()).save(any());
    }
}
