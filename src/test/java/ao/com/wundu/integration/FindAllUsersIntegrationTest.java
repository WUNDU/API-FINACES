package ao.com.wundu.integration;

import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.usercases.user.FindAllUsersUseCase;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.naming.ServiceUnavailableException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class FindAllUsersIntegrationTest {

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
    private FindAllUsersUseCase findAllUsersUseCase;

    @Test
    void shouldFindAllUsersSuccessfully() throws ServiceUnavailableException {
        // Registrar dois usuários
        UserCreateDTO createDTO1 = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "+244923456789",
                "email"
        );
        UserCreateDTO createDTO2 = new UserCreateDTO(
                "Jane Doe",
                "jane.doe@example.com",
                "Password123",
                "+244987654321",
                "sms"
        );
        registerUserUseCase.execute(createDTO1);
        registerUserUseCase.execute(createDTO2);

        // Buscar todos os usuários
        Page<UserResponseDTO> users = findAllUsersUseCase.execute(PageRequest.of(0, 10));

        // Verificar resposta
        assertNotNull(users);
        assertEquals(2, users.getTotalElements());
        assertEquals(1, users.getTotalPages());
        assertTrue(users.getContent().stream().anyMatch(u -> u.email().equals("john.doe@example.com")));
        assertTrue(users.getContent().stream().anyMatch(u -> u.email().equals("jane.doe@example.com")));
    }

}
