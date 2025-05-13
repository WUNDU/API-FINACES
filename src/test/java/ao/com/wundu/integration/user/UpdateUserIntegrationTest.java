package ao.com.wundu.integration.user;

import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.dtos.user.UserUpdateDTO;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
import ao.com.wundu.application.usercases.user.UpdateUserUseCase;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.naming.ServiceUnavailableException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class UpdateUserIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("finances")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private UpdateUserUseCase updateUserUseCase;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldUpdateUserSuccessfully() throws ServiceUnavailableException {
        // Registrar usuário
        UserCreateDTO createDTO = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "+244923456789",
                "email"
        );
        UserResponseDTO createdUser = registerUserUseCase.execute(createDTO);

        // Atualizar usuário
        UserUpdateDTO updateDTO = new UserUpdateDTO(
                "john Doe",
                "+244987654321",
                "sms"
        );
        UserResponseDTO updatedUser = updateUserUseCase.execute(createdUser.id(), updateDTO);

        // Verificar resposta
        assertNotNull(updatedUser);
        assertEquals(createdUser.id(), updatedUser.id());
        assertEquals("john Doe", updatedUser.name());
        assertEquals("john.doe@example.com", updatedUser.email());
        assertEquals("+244987654321", updatedUser.phone());
        assertEquals("sms", updatedUser.notificationPreference());
        assertEquals("free", updatedUser.planType());
        assertEquals(0, updatedUser.creditCardCount());

        // Verificar no banco
        User savedUser = userRepository.findById(createdUser.id()).orElse(null);
        assertNotNull(savedUser);
        assertEquals("john Doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
        assertEquals("+244987654321", savedUser.getPhone());
        assertEquals("sms", savedUser.getNotificationPreference().getValue());
    }
}
