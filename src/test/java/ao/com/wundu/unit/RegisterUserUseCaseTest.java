package ao.com.wundu.unit;

import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.mappers.UserMapper;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.domain.enums.PlanType;
import ao.com.wundu.domain.exceptions.DuplicateEmailException;
import ao.com.wundu.domain.exceptions.DuplicatePhoneException;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.EmailService;
import ao.com.wundu.infrastructure.services.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.ServiceUnavailableException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RegisterUserUseCase useCase;

    private UserCreateDTO dto;
    private User user;
    private String userId;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
        dto = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "+244923456789",
                "email");

        user = new User(
                "John Doe",
                "john.doe@example.com",
                "encoded",
                "+244923456789",
                NotificationPreference.EMAIL
        );

        responseDTO = new UserResponseDTO(
                userId,
                "John Doe",
                "john.doe@example.com",
                "+244923456789",
                "email",
                "free",
                0);
    }

    @Test
    void shouldRegisterUserSuccessfully() throws ServiceUnavailableException {
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userRepository.existsByPhone(dto.phone())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.password())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        UserResponseDTO result = useCase.execute(dto);

        assertEquals(responseDTO, result);
        verify(userRepository).save(user);
        verify(emailService).sendEmail(anyString(), anyString(), anyString());
        assertTrue(user.isDataConsent());
        assertFalse(user.isDeleted());
        assertFalse(user.isLocked());
        assertEquals(0, user.getLoginAttempts());
        assertEquals(PlanType.FREE, user.getPlanType());
        assertNull(user.getLockedUntil());
        assertTrue(user.getCreditCards().isEmpty());
    }

    @Test
    void shouldThrowDuplicateEmailException() {
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> useCase.execute(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowDuplicatePhoneException() {
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userRepository.existsByPhone(dto.phone())).thenReturn(true);

        assertThrows(DuplicatePhoneException.class, () -> useCase.execute(dto));
        verify(userRepository, never()).save(any());
    }
}
