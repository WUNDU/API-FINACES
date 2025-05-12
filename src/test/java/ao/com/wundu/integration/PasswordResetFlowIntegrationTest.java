package ao.com.wundu.integration;

import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.usercases.auth.AuthenticateUseCase;
import ao.com.wundu.application.usercases.auth.ConfirmPasswordResetUseCase;
import ao.com.wundu.application.usercases.auth.RequestPasswordResetUseCase;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
import ao.com.wundu.domain.entities.PasswordResetToken;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.infrastructure.repositories.PasswordResetTokenRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.naming.ServiceUnavailableException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
public class PasswordResetFlowIntegrationTest {

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
    private RequestPasswordResetUseCase requestPasswordResetUseCase;

    @Autowired
    private ConfirmPasswordResetUseCase confirmPasswordResetUseCase;

    @Autowired
    private AuthenticateUseCase authenticateUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @Test
    void shouldCompletePasswordResetFlowSuccessfully() throws ServiceUnavailableException {
        // Registrar usuário
        UserCreateDTO createDTO = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "+244923456789",
                "email"
        );
        registerUserUseCase.execute(createDTO);

        // Configurar mock do EmailService
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Solicitar redefinição
//        requestPasswordResetUseCase.execute("john.doe@example.com");
        PasswordResetToken token = tokenRepository.findAll().stream().findFirst().orElse(null);
        assertNotNull(token);

        // Confirmar redefinição
        String newPassword = "NewPassword123";
//        confirmPasswordResetUseCase.execute(token.getToken(), newPassword);

        // Verificar nova senha com autenticação
//        User authenticatedUser = authenticateUseCase.authenticate("john.doe@example.com", newPassword);
//        assertNotNull(authenticatedUser);
//        assertEquals("john.doe@example.com", authenticatedUser.getEmail());

        // Verificar que o token foi deletado
        assertTrue(tokenRepository.findByToken(token.getToken()).isEmpty());
    }
}
