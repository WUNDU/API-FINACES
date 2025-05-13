package ao.com.wundu.integration.user;

import ao.com.wundu.application.dtos.ResetPassword.PasswordResetConfirmDTO;
import ao.com.wundu.application.dtos.ResetPassword.PasswordResetRequestDTO;
import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.usercases.auth.ConfirmPasswordResetUseCase;
import ao.com.wundu.application.usercases.auth.RequestPasswordResetUseCase;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
import ao.com.wundu.domain.entities.PasswordResetToken;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.infrastructure.repositories.PasswordResetTokenRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.impl.EmailServiceImpl;
import ao.com.wundu.infrastructure.services.impl.SmsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.naming.ServiceUnavailableException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Testcontainers
public class ConfirmPasswordResetIntegrationTest {

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
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private EmailServiceImpl emailService;

    @MockitoBean
    private SmsServiceImpl smsService;

    @Test
    void shouldConfirmPasswordResetSuccessfully() throws ServiceUnavailableException {
        // Mockar serviços de e-mail e SMS
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        doNothing().when(smsService).sendSms(anyString(), anyString());

        // Registrar usuário
        UserCreateDTO createDTO = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "+244923456789",
                "email"
        );
        registerUserUseCase.execute(createDTO);

        // Solicitar redefinição
        PasswordResetRequestDTO resetRequestDTO = new PasswordResetRequestDTO("john.doe@example.com", "email");
        requestPasswordResetUseCase.execute(resetRequestDTO);
        PasswordResetToken token = tokenRepository.findAll().stream().findFirst().orElse(null);
        assertNotNull(token);

        // Confirmar redefinição
        String newPassword = "NewPassword123";
        PasswordResetConfirmDTO confirmDto = new PasswordResetConfirmDTO(token.getToken(), newPassword);
        confirmPasswordResetUseCase.execute(confirmDto);

        // Verificar nova senha
        User updatedUser = userRepository.findByEmail("john.doe@example.com").orElse(null);
        assertNotNull(updatedUser);
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));

        // Verificar que o token foi deletado
        assertTrue(tokenRepository.findByToken(token.getToken()).isEmpty());
    }
}
