package ao.com.wundu.unit;

import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.mappers.UserMapper;
import ao.com.wundu.application.usercases.user.FindUserByIdUseCase;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private FindUserByIdUseCase useCase;

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
        responseDTO = new UserResponseDTO("1", "John Doe", "john.doe@example.com", "+244923456789", "email", "free", 0);
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = useCase.execute("1");

        assertEquals(responseDTO, result);
        verify(userRepository).findById("1");
    }

    @Test
    void shouldThrowUserNotFoundException() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute("1"));
    }
}
