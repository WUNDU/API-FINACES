package ao.com.wundu.unit.card;

import ao.com.wundu.application.dtos.card.CreditCardCreateDTO;
import ao.com.wundu.application.dtos.card.CreditCardResponseDTO;
import ao.com.wundu.application.mappers.CreditCardMapper;
import ao.com.wundu.application.usercases.card.AddCreditCardUseCase;
import ao.com.wundu.domain.entities.CreditCard;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.PlanType;
import ao.com.wundu.domain.exceptions.CreditCardLimitExceededException;
import ao.com.wundu.domain.exceptions.InvalidCardException;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.service.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddCreditCardUseCaseTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private CreditCardMapper creditCardMapper;

    private AddCreditCardUseCase useCase;

    private User user;
    private CreditCardCreateDTO validDto;
    private CreditCard card;

    @BeforeEach
    void setUp() {

        // Inicialização manual de AddCreditCardUseCase com valores para freePlanLimit e premiumPlanLimit
        useCase = new AddCreditCardUseCase(
                creditCardRepository,
                userRepository,
                encryptionService,
                creditCardMapper,
                3, // freePlanLimit
                10 // premiumPlanLimit
        );

        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setPlanType(PlanType.FREE);
        validDto = new CreditCardCreateDTO(
                "4532015112830366", // Número válido pelo algoritmo de Luhn
                "Test Bank",
                new BigDecimal("1000.00"),
                LocalDate.now().plusMonths(1)
        );
        card = new CreditCard();
        card.setId(UUID.randomUUID().toString());
        card.setCardNumber(validDto.cardNumber());
        card.setBankName(validDto.bankName());
        card.setCreditLimit(validDto.creditLimit());
        card.setExpirationDate(validDto.expirationDate());
        card.setUser(user);
    }

    @Test
    void shouldAddCreditCardSuccessfully() throws Exception {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(creditCardRepository.findByUserId(user.getId())).thenReturn(List.of());
        when(encryptionService.encrypt(validDto.cardNumber())).thenReturn("encrypted-number");
        when(creditCardMapper.toEntity(validDto)).thenReturn(card);
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(card);
        when(creditCardMapper.toResponseDTO(card)).thenReturn(new CreditCardResponseDTO(
                card.getId(),
                card.getMaskedCardNumber(),
                card.getBankName(),
                card.getCreditLimit(),
                card.getFormattedExpirationDate(),
                user.getId()
        ));

        CreditCardResponseDTO response = useCase.execute(user.getId(), validDto);

        assertNotNull(response);
        assertEquals(card.getId(), response.id());
        assertEquals("**** **** **** 0366", response.maskedCardNumber());
        verify(creditCardRepository).save(any(CreditCard.class));
    }

    @Test
    void shouldThrowInvalidCardExceptionForInvalidNumber() {
        CreditCardCreateDTO invalidDto = new CreditCardCreateDTO(
                "4532015112830367", // Número inválido pelo Luhn
                validDto.bankName(),
                validDto.creditLimit(),
                validDto.expirationDate()
        );

        assertThrows(InvalidCardException.class, () -> useCase.execute(user.getId(), invalidDto));
    }

    @Test
    void shouldThrowInvalidCardExceptionForExpiredDate() {
        CreditCardCreateDTO invalidDto = new CreditCardCreateDTO(
                validDto.cardNumber(),
                validDto.bankName(),
                validDto.creditLimit(),
                LocalDate.now().minusMonths(1)
        );

        assertThrows(InvalidCardException.class, () -> useCase.execute(user.getId(), invalidDto));
    }

    @Test
    void shouldThrowUserNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(user.getId(), validDto));
    }

    @Test
    void shouldThrowCreditCardLimitExceededExceptionForFreePlan() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(creditCardRepository.findByUserId(user.getId())).thenReturn(List.of(
                new CreditCard(), new CreditCard(), new CreditCard()
        ));

        assertThrows(CreditCardLimitExceededException.class, () -> useCase.execute(user.getId(), validDto));
    }

    @Test
    void shouldThrowCreditCardLimitExceededExceptionForPremiumPlan() {
        user.setPlanType(PlanType.PREMIUM);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(creditCardRepository.findByUserId(user.getId())).thenReturn(List.of(
                new CreditCard(), new CreditCard(), new CreditCard(), new CreditCard(), new CreditCard(),
                new CreditCard(), new CreditCard(), new CreditCard(), new CreditCard(), new CreditCard()
        ));

        assertThrows(CreditCardLimitExceededException.class, () -> useCase.execute(user.getId(), validDto));
    }

    @Test
    void shouldAllowMaxCardsForPremiumPlan() throws Exception {
        user.setPlanType(PlanType.PREMIUM);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(creditCardRepository.findByUserId(user.getId())).thenReturn(List.of(
                new CreditCard(), new CreditCard(), new CreditCard(), new CreditCard(), new CreditCard(),
                new CreditCard(), new CreditCard(), new CreditCard(), new CreditCard()
        ));
        when(encryptionService.encrypt(validDto.cardNumber())).thenReturn("encrypted-number");
        when(creditCardMapper.toEntity(validDto)).thenReturn(card);
        when(creditCardRepository.save(any(CreditCard.class))).thenReturn(card);
        when(creditCardMapper.toResponseDTO(card)).thenReturn(new CreditCardResponseDTO(
                card.getId(),
                card.getMaskedCardNumber(),
                card.getBankName(),
                card.getCreditLimit(),
                card.getFormattedExpirationDate(),
                user.getId()
        ));

        CreditCardResponseDTO response = useCase.execute(user.getId(), validDto);

        assertNotNull(response);
        verify(creditCardRepository).save(any(CreditCard.class));
    }
}
