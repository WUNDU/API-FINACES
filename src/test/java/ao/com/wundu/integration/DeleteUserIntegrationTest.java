package ao.com.wundu.integration;

import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.usercases.user.DeleteUserUseCase;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class DeleteUserIntegrationTest {

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
    private DeleteUserUseCase deleteUserUseCase;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldDeleteUserSuccessfully() throws ServiceUnavailableException {
        // Registrar usuário
        UserCreateDTO createDTO = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "+244923456789",
                "email"
        );
        UserResponseDTO createdUser = registerUserUseCase.execute(createDTO);

        // Deletar usuário
        deleteUserUseCase.execute(createdUser.id());

        // Verificar no banco
        User deletedUser = userRepository.findById(createdUser.id()).orElse(null);
        assertNotNull(deletedUser);
        assertTrue(((User) deletedUser).isDeleted());
    }
}
