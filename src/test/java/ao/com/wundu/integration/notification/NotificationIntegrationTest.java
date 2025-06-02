//package ao.com.wundu.integration.notification;
//
//import ao.com.wundu.application.dtos.notification.NotificationPreferenceDTO;
//import ao.com.wundu.application.dtos.transation.TransactionCreateDTO;
//import ao.com.wundu.application.dtos.transation.TransactionResponseDTO;
//import ao.com.wundu.application.dtos.notification.NotificationResponseDTO;
//import ao.com.wundu.application.dtos.user.UserCreateDTO;
//import ao.com.wundu.domain.entities.CreditCard;
//import ao.com.wundu.domain.entities.Notification;
//import ao.com.wundu.domain.entities.Transaction;
//import ao.com.wundu.domain.entities.User;
//import ao.com.wundu.domain.enums.NotificationPreference;
//import ao.com.wundu.domain.enums.PlanType;
//import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
//import ao.com.wundu.infrastructure.repositories.NotificationRepository;
//import ao.com.wundu.infrastructure.repositories.TransactionRepository;
//import ao.com.wundu.infrastructure.repositories.UserRepository;
//import ao.com.wundu.integration.card.CreditCardIntegrationTest;
//import jakarta.mail.internet.MimeMessage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.http.*;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.transaction.annotation.Transactional;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Testcontainers
//@Transactional
//public class NotificationIntegrationTest {
//
//    private static final Logger logger = LoggerFactory.getLogger(NotificationIntegrationTest.class);
//
//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
//            .withDatabaseName("finances")
//            .withUsername("user")
//            .withPassword("password");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
//        registry.add("spring.flyway.enabled", () -> "true");
//    }
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CreditCardRepository creditCardRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @MockBean
//    private JavaMailSender javaMailSender;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    private UserCreateDTO userCreateDTO;
//
//    private String userId;
//    private String creditCardId;
//    private String transactionId;
//    private String jwtToken;
//
//    record LoginDTO(String email, String password) {}
//    record LoginResponseDTO(String token) {}
//
//    @BeforeEach
//    void setUp(){
//
//        notificationRepository.deleteAll();
//        transactionRepository.deleteAll();
//        creditCardRepository.deleteAll();
//        userRepository.deleteAll();
//
//
//        User user = new User();
//        String email = "test-" + UUID.randomUUID() + "@example.com";
//        String rawPassword = "password123";
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(rawPassword)); // Codificar a senha
//        user.setName("Test User");
//        user.setNotificationPreference(NotificationPreference.SMS);
//        user.setPlanType(PlanType.FREE);
//        user.setPhone("+2449" + (900000000 + (int)(Math.random() * 100000000)));
//        user = userRepository.save(user);
//        userId = user.getId();
//
//        // Fazer login para obter o token JWT
//        HttpHeaders loginHeaders = new HttpHeaders();
//        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
//        String loginBody = "{\"email\": \"" + email + "\", \"password\": \"" + rawPassword + "\"}";
//        HttpEntity<String> loginRequest = new HttpEntity<>(loginBody, loginHeaders);
//        logger.info("Enviando requisição de login para /api/auth/login com corpo: {}", loginBody);
//        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
//                "/api/auth/login",
//                loginRequest,
//                String.class
//        );
//        logger.info("Resposta do login: status={}, corpo={}", loginResponse.getStatusCode(), loginResponse.getBody());
//        assertEquals(HttpStatus.OK, loginResponse.getStatusCode(), "Falha no login: " + loginResponse.getBody());
//        String responseBody = loginResponse.getBody();
//        jwtToken = responseBody != null && responseBody.contains("token") ?
//                responseBody.split("\"token\":\"")[1].split("\"")[0] :
//                null;
//        assertNotNull(jwtToken, "JWT token não encontrado na resposta: " + responseBody);
//
//        CreditCard creditCard = new CreditCard();
//        creditCard.setUser(user);
//        creditCard.setCardNumber("4532015112830366");
//        creditCard.setBankName("Test Bank");
//        creditCard.setCreditLimit(new BigDecimal("1000.00"));
//        creditCard.setExpirationDate(LocalDate.now().plusMonths(1));
//        creditCard = creditCardRepository.save(creditCard);
//        creditCardId = creditCard.getId();
//
//        Transaction transaction = new Transaction();
//        transaction.setAmount(100.0);
//        transaction.setDescription("Compra");
//        transaction.setType("DEBIT");
//        transaction.setCreditCard(creditCard);
//        transaction = transactionRepository.save(transaction);
//        transactionId = transaction.getId();
//    }
//
//    private <T> HttpEntity<T> createAuthenticatedRequest(T body) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(jwtToken); // Ajuste conforme o mecanismo de autenticação
//        return new HttpEntity<>(body, headers);
//    }
//
//    @Test
//    void shouldSendNotificationWhenTransactionIsCreated() {
//        TransactionCreateDTO dto = new TransactionCreateDTO(200.0, "Compra", "DEBIT");
//
//        ResponseEntity<TransactionResponseDTO> response = restTemplate.exchange(
//                "/api/transactions/credit-card/" + creditCardId,
//                HttpMethod.POST,
//                createAuthenticatedRequest(dto),
//                TransactionResponseDTO.class
//        );
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//
//        Notification notification = notificationRepository.findByUserId(userId).get(0);
//        assertEquals("SENT", notification.getStatus());
//        assertEquals(NotificationPreference.SMS, notification.getChannel());
//        assertFalse(notification.isRead());
//        assertEquals(response.getBody().id(), notification.getTransaction().getId());
//    }
//
//    @Test
//    void shouldSendEmailNotificationWhenPreferenceIsEmail() {
//        User user = userRepository.findById(userId).orElseThrow();
//        user.setNotificationPreference(NotificationPreference.EMAIL);
//        userRepository.save(user);
//
//        TransactionCreateDTO txDto = new TransactionCreateDTO(200.0, "Compra", "DEBIT");
//
//        ResponseEntity<TransactionResponseDTO> response = restTemplate.exchange(
//                "/api/transactions/credit-card/" + creditCardId,
//                HttpMethod.POST,
//                createAuthenticatedRequest(txDto),
//                TransactionResponseDTO.class
//        );
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//
//        Notification notification = notificationRepository.findByUserId(userId).get(0);
//        assertEquals("SENT", notification.getStatus());
//        assertEquals(NotificationPreference.EMAIL, notification.getChannel());
//        assertFalse(notification.isRead());
//        assertEquals(response.getBody().id(), notification.getTransaction().getId());
//        verify(javaMailSender).send(any(MimeMessage.class));
//    }
//
//    @Test
//    void shouldMarkNotificationAsRead() {
//        Notification notification = new Notification();
//        notification.setId(UUID.randomUUID().toString());
//        notification.setUser(userRepository.findById(userId).orElseThrow());
//        notification.setTransaction(transactionRepository.findById(transactionId).orElseThrow());
//        notification.setChannel(NotificationPreference.SMS);
//        notification.setMessage("Transação de 100.0 realizada.");
//        notification.setStatus("SENT");
//        notification = notificationRepository.save(notification);
//
//        ResponseEntity<NotificationResponseDTO> response = restTemplate.exchange(
//                "/api/notifications/" + notification.getId() + "/read",
//                HttpMethod.PATCH,
//                createAuthenticatedRequest(null),
//                NotificationResponseDTO.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().isRead());
//    }
//}
