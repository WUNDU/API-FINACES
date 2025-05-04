package ao.com.wundu.integration;

import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.usercases.user.FindUserByIdUseCase;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
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
public class FindUserByIdIntegrationTest {

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
    private FindUserByIdUseCase findUserByIdUseCase;

    @Test
    void shouldFindUserByIdSuccessfully() throws ServiceUnavailableException {
        // Registrar usuário
        UserCreateDTO createDTO = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "+244923456789",
                "email"
        );
        UserResponseDTO createdUser = registerUserUseCase.execute(createDTO);

        // Buscar usuário
        UserResponseDTO foundUser = findUserByIdUseCase.execute(createdUser.id());

        // Verificar resposta
        assertNotNull(foundUser);
        assertEquals(createdUser.id(), foundUser.id());
        assertEquals("John Doe", foundUser.name());
        assertEquals("john.doe@example.com", foundUser.email());
        assertEquals("+244923456789", foundUser.phone());
        assertEquals("email", foundUser.notificationPreference());
        assertEquals("free", foundUser.planType());
        assertEquals(0, foundUser.creditCardCount());
    }

    @Test
    void shouldThrowUserNotFoundExceptionForInvalidId() {
        assertThrows(UserNotFoundException.class, () -> findUserByIdUseCase.execute("invalid-id"));
    }
}
