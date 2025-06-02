package ao.com.wundu.integration.card;

import ao.com.wundu.application.dtos.card.CreditCardCreateDTO;
import ao.com.wundu.application.dtos.card.CreditCardResponseDTO;
import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.usercases.user.RegisterUserUseCase;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.PlanType;
import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.impl.EmailServiceImpl;
import ao.com.wundu.service.EncryptionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CreditCardIntegrationTest {

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
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @MockBean
    private EmailServiceImpl emailService;

    @MockBean
    private EncryptionService encryptionService;

    private UserCreateDTO userCreateDTO;
    private String userId;
    private String jwtToken;
    private int encryptionCounter = 0;

    record LoginDTO(String email, String password) {}
    record LoginResponseDTO(String token) {}

    @BeforeEach
    void setUp() throws Exception {
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        when(encryptionService.encrypt(anyString())).thenAnswer(invocation -> "encrypted-number-" + (++encryptionCounter));
        creditCardRepository.deleteAll();
        userRepository.deleteAll();

        userCreateDTO = new UserCreateDTO(
                "Test User",
                "testuser_" + UUID.randomUUID() + "@example.com",
                "password123",
                "+244923456789",
                "email"
        );
        userId = registerUserUseCase.execute(userCreateDTO).id();

        LoginDTO loginDTO = new LoginDTO(userCreateDTO.email(), userCreateDTO.password());
        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "/api/auth/login",
                loginDTO,
                LoginResponseDTO.class
        );
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode(), "Falha ao obter token JWT: " + loginResponse.getBody());
        assertNotNull(loginResponse.getBody(), "Resposta de login vazia");
        jwtToken = loginResponse.getBody().token();
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        userRepository.deleteAll();
        encryptionCounter = 0;
    }

    private <T> HttpEntity<T> createAuthenticatedRequest(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        return new HttpEntity<>(body, headers);
    }

    @Test
    void shouldAddAndListCreditCardsSuccessfully() {
        CreditCardCreateDTO cardDTO = new CreditCardCreateDTO(
                "4532015112830366",
                "Test Bank",
                LocalDate.now().plusMonths(1)
        );

        ResponseEntity<CreditCardResponseDTO> response = restTemplate.exchange(
                "/api/credit-cards/user/" + userId,
                HttpMethod.POST,
                createAuthenticatedRequest(cardDTO),
                CreditCardResponseDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Erro: " + response.getBody());
        assertNotNull(response.getBody(), "Resposta vazia");
        System.out.println("Masked Card Number: " + response.getBody().maskedCardNumber());
        assertEquals("**** **** **** 0366", response.getBody().maskedCardNumber());

        ResponseEntity<List> listResponse = restTemplate.exchange(
                "/api/credit-cards/user/" + userId,
                HttpMethod.GET,
                createAuthenticatedRequest(null),
                List.class
        );

        assertEquals(HttpStatus.OK, listResponse.getStatusCode(), "Erro: " + listResponse.getBody());
        assertNotNull(listResponse.getBody());
        assertEquals(1, listResponse.getBody().size());
    }

    @Test
    void shouldRejectInvalidCardNumber() {
        CreditCardCreateDTO invalidCardDTO = new CreditCardCreateDTO(
                "4532015112830367",
                "Test Bank",
                LocalDate.now().plusMonths(1)
        );

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/credit-cards/user/" + userId,
                HttpMethod.POST,
                createAuthenticatedRequest(invalidCardDTO),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Erro: " + response.getBody());
        assertTrue(response.getBody().contains("Número de cartão inválido"));
    }

    @Test
    void shouldRejectExpiredCard() {
        LocalDate expiredDate = LocalDate.now().minusMonths(1);
        CreditCardCreateDTO expiredCardDTO = new CreditCardCreateDTO(
                "4532015112830366",
                "Test Bank",
                expiredDate
        );

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/credit-cards/user/" + userId,
                HttpMethod.POST,
                createAuthenticatedRequest(expiredCardDTO),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Erro: " + response.getBody());
        System.out.println("Resposta do servidor: " + response.getBody());
        System.out.println("Data de expiração enviada: " + expiredDate);
        assertTrue(response.getBody().contains("Data de expiração deve ser futura"));
    }

    @Test
    void shouldRejectCardWhenLimitExceeded() {
        CreditCardCreateDTO cardDTO = new CreditCardCreateDTO(
                "4532015112830366",
                "Test Bank",
                LocalDate.now().plusMonths(1)
        );
        for (int i = 0; i < 1; i++) { // Limite do plano FREE é 1
            ResponseEntity<CreditCardResponseDTO> response = restTemplate.exchange(
                    "/api/credit-cards/user/" + userId,
                    HttpMethod.POST,
                    createAuthenticatedRequest(cardDTO),
                    CreditCardResponseDTO.class
            );
            assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Erro ao adicionar cartão " + (i + 1) + ": " + response.getBody());
        }

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/credit-cards/user/" + userId,
                HttpMethod.POST,
                createAuthenticatedRequest(cardDTO),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Erro: " + response.getBody());
        assertTrue(response.getBody().contains("Limite de 1 cartões atingido"));
    }

    @Test
    void shouldAllowMoreCardsForPremiumUser() {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPlanType(PlanType.PREMIUM);
        userRepository.save(user);

        CreditCardCreateDTO cardDTO = new CreditCardCreateDTO(
                "4532015112830366",
                "Test Bank",
                LocalDate.now().plusMonths(1)
        );
        for (int i = 0; i < 3; i++) { // Limite do plano PREMIUM é 3
            ResponseEntity<CreditCardResponseDTO> response = restTemplate.exchange(
                    "/api/credit-cards/user/" + userId,
                    HttpMethod.POST,
                    createAuthenticatedRequest(cardDTO),
                    CreditCardResponseDTO.class
            );
            assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Erro ao adicionar cartão " + (i + 1) + ": " + response.getBody());
        }

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/credit-cards/user/" + userId,
                HttpMethod.POST,
                createAuthenticatedRequest(cardDTO),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Erro: " + response.getBody());
        assertTrue(response.getBody().contains("Limite de 3 cartões atingido"));
    }
}
