package ao.com.wundu.integration.user;

import ao.com.wundu.application.dtos.ResetPassword.PasswordResetConfirmDTO;
import ao.com.wundu.application.dtos.ResetPassword.PasswordResetRequestDTO;
import ao.com.wundu.application.dtos.auth.LoginRequestDTO;
import ao.com.wundu.application.dtos.auth.LoginResponseDTO;
import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
import ao.com.wundu.domain.entities.PasswordResetToken;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.infrastructure.repositories.PasswordResetTokenRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.EmailService;
import ao.com.wundu.infrastructure.services.SmsService;
import ao.com.wundu.infrastructure.services.impl.EmailServiceImpl;
import ao.com.wundu.infrastructure.services.impl.SmsServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AuthIntegrationTest {

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
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegisterUserUseCase registerUserUseCase;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @MockBean
    private EmailServiceImpl emailService;

    @MockBean
    private SmsServiceImpl smsService;

    @Test
    void shouldAuthenticateSuccessfully() throws ServiceUnavailableException {
        // Registrar usuário
        UserCreateDTO createDTO = new UserCreateDTO(
                "John Doe",
                "john.doe@example.com",
                "Password123",
                "+244923456789",
                "email"
        );
        registerUserUseCase.execute(createDTO);

        // Tentar autenticar
        LoginRequestDTO loginDTO = new LoginRequestDTO("john.doe@example.com", "Password123");
        ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(
                "/api/auth/login",
                loginDTO,
                LoginResponseDTO.class
        );

        // Verificar resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().token());
        assertNotNull(response.getBody().refreshToken());
        assertEquals("john.doe@example.com", response.getBody().user().email());
    }

    @Test
    void shouldReturnUnauthorizedForInvalidCredentials() {
        // Tentar autenticar com credenciais inválidas
        LoginRequestDTO loginDTO = new LoginRequestDTO("john.doe@example.com", "WrongPassword");
        ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(
                "/api/auth/login",
                loginDTO,
                LoginResponseDTO.class
        );

        // Verificar resposta
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldRequestAndConfirmPasswordResetSuccessfully() throws ServiceUnavailableException {
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

        // Solicitar redefinição de senha
        PasswordResetRequestDTO requestDto = new PasswordResetRequestDTO("john.doe@example.com", "email");
        ResponseEntity<Void> requestResponse = restTemplate.postForEntity(
                "/api/auth/password/reset-request",
                requestDto,
                Void.class
        );
        assertEquals(HttpStatus.OK, requestResponse.getStatusCode());

        // Confirmar redefinição de senha
        PasswordResetToken token = tokenRepository.findAll().get(0);
        PasswordResetConfirmDTO confirmDto = new PasswordResetConfirmDTO(token.getToken(), "NewPassword123");
        ResponseEntity<Void> confirmResponse = restTemplate.postForEntity(
                "/api/auth/password/reset-confirm",
                confirmDto,
                Void.class
        );
        assertEquals(HttpStatus.OK, confirmResponse.getStatusCode());

        // Verificar usuário atualizado
        User updatedUser = userRepository.findByEmail("john.doe@example.com").orElse(null);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getLoginAttempts());
        assertFalse(updatedUser.isLocked());

        // Verificar que o token foi removido
        assertTrue(tokenRepository.findByToken(token.getToken()).isEmpty());
    }
}
