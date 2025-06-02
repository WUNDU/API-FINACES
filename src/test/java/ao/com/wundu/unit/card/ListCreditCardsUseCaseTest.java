//package ao.com.wundu.unit.card;
//
//import ao.com.wundu.application.dtos.card.CreditCardResponseDTO;
//import ao.com.wundu.application.mappers.CreditCardMapper;
//import ao.com.wundu.application.usercases.card.ListCreditCardsUseCase;
//import ao.com.wundu.domain.entities.CreditCard;
//import ao.com.wundu.domain.entities.User;
//import ao.com.wundu.domain.exceptions.UserNotFoundException;
//import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
//import ao.com.wundu.infrastructure.repositories.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ListCreditCardsUseCaseTest {
//
//    @Mock
//    private CreditCardRepository creditCardRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private CreditCardMapper creditCardMapper;
//
//    @InjectMocks
//    private ListCreditCardsUseCase useCase;
//
//    private User user;
//    private CreditCard card;
//
//    @BeforeEach
//    void setUp() {
//        user = new User();
//        user.setId(UUID.randomUUID().toString());
//        card = new CreditCard(
//                "4532015112830366",
//                "Test Bank",
//                new BigDecimal("1000.00"),
//                LocalDate.of(2025, 12, 1),
//                user
//        );
//        card.setId(UUID.randomUUID().toString());
//    }
//
//    @Test
//    void shouldListCreditCardsSuccessfully() {
//        when(userRepository.existsById(user.getId())).thenReturn(true);
//        when(creditCardRepository.findByUserId(user.getId())).thenReturn(List.of(card));
//        when(creditCardMapper.toResponseDTO(card)).thenReturn(new CreditCardResponseDTO(
//                card.getId(),
//                card.getMaskedCardNumber(),
//                card.getBankName(),
//                card.getCreditLimit(),
//                card.getFormattedExpirationDate(),
//                user.getId()
//        ));
//
//        List<CreditCardResponseDTO> response = useCase.execute(user.getId());
//
//        assertNotNull(response);
//        assertEquals(1, response.size());
//        assertEquals(card.getId(), response.get(0).id());
//        assertEquals("**** **** **** 0366", response.get(0).maskedCardNumber());
//    }
//
//    @Test
//    void shouldThrowUserNotFoundException() {
//        when(userRepository.existsById(user.getId())).thenReturn(false);
//
//        assertThrows(UserNotFoundException.class, () -> useCase.execute(user.getId()));
//    }
//
//    @Test
//    void shouldReturnEmptyListForNoCards() {
//        when(userRepository.existsById(user.getId())).thenReturn(true);
//        when(creditCardRepository.findByUserId(user.getId())).thenReturn(List.of());
//
//        List<CreditCardResponseDTO> response = useCase.execute(user.getId());
//
//        assertNotNull(response);
//        assertTrue(response.isEmpty());
//    }
//}
