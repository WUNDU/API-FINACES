package ao.com.wundu.integration.user;

import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.naming.ServiceUnavailableException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserIntegrationTest {

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
    private UserRepository userRepository;

    @Test
    void shouldRegisterUserSuccessfully() throws ServiceUnavailableException {
        UserCreateDTO dto = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "+244923456789",
                "email"
        );

        UserResponseDTO response = registerUserUseCase.execute(dto);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john.doe@example.com", response.email());
        assertEquals("+244923456789", response.phone());
        assertEquals("email", response.notificationPreference());
        assertEquals("free", response.planType());
        assertEquals(0, response.creditCardCount());

        User savedUser = userRepository.findByEmail("john.doe@example.com").orElse(null);
        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        assertTrue(savedUser.isDataConsent());
        assertFalse(savedUser.isDeleted());
        assertFalse(savedUser.isLocked());
        assertEquals(0, savedUser.getLoginAttempts());
    }
}
