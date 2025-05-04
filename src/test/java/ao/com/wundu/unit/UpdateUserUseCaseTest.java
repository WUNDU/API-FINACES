package ao.com.wundu.unit;

import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.dtos.user.UserUpdateDTO;
import ao.com.wundu.application.mappers.UserMapper;
import ao.com.wundu.application.usercases.user.UpdateUserUseCase;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.domain.exceptions.DuplicatePhoneException;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UpdateUserUseCase useCase;

    private UserUpdateDTO dto;
    private User user;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = new User(
                "John Doe",
                "john.doe@example.com",
                "encoded",
                "+244923456789",
                NotificationPreference.EMAIL
        );
        user.setId("1");
        dto = new UserUpdateDTO("Jane Doe", "+244987654321", "sms");
        responseDTO = new UserResponseDTO("1", "Jane Doe", "john.doe@example.com", "+244987654321", "sms", "free", 0);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.existsByPhone(dto.phone())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId("1");
            return savedUser;
        });
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(responseDTO);

        UserResponseDTO result = useCase.execute("1", dto);

        assertNotNull(result);
        assertEquals(responseDTO, result);

        verify(userRepository).findById("1");
        verify(userRepository).existsByPhone(dto.phone());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toResponseDTO(any(User.class));

        assertEquals(dto.name(), user.getName());
        assertEquals(dto.phone(), user.getPhone());
        assertEquals(NotificationPreference.fromValue(dto.notificationPreference()), user.getNotificationPreference());
    }

    @Test
    void shouldThrowUserNotFoundException() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute("1", dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowDuplicatePhoneException() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.existsByPhone(dto.phone())).thenReturn(true);

        assertThrows(DuplicatePhoneException.class, () -> useCase.execute("1", dto));
        verify(userRepository, never()).save(any());
    }
}
