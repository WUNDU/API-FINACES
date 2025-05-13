package ao.com.wundu.unit.user;

import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.mappers.UserMapper;
import ao.com.wundu.application.usercases.user.FindAllUsersUseCase;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAllUsersUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private FindAllUsersUseCase useCase;

    private User user;
    private UserResponseDTO responseDTO;
    private Pageable pageable;

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
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void shouldFindAllUsersSuccessfully() {
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        Page<UserResponseDTO> result = useCase.execute(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(responseDTO, result.getContent().get(0));
        verify(userRepository).findAll(pageable);
    }
}
