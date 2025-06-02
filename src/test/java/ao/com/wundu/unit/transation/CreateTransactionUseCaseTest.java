//package ao.com.wundu.unit.transation;
//
//import ao.com.wundu.application.dtos.transation.TransactionCreateDTO;
//import ao.com.wundu.application.dtos.transation.TransactionResponseDTO;
//import ao.com.wundu.application.usercases.notification.SendTransactionNotificationUseCase;
//import ao.com.wundu.application.usercases.transaction.CreateTransactionUseCase;
//import ao.com.wundu.domain.entities.CreditCard;
//import ao.com.wundu.domain.entities.Transaction;
//import ao.com.wundu.domain.entities.User;
//import ao.com.wundu.domain.exceptions.CreditCardNotFoundException;
//import ao.com.wundu.domain.exceptions.InvalidTransactionAmountException;
//import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
//import ao.com.wundu.infrastructure.repositories.TransactionRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CreateTransactionUseCaseTest {
//
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @Mock
//    private CreditCardRepository creditCardRepository;
//
//    @Mock
//    private SendTransactionNotificationUseCase notificationUseCase;
//
//    @InjectMocks
//    private CreateTransactionUseCase useCase;
//
//    @Test
//    void shouldCreateTransactionSuccessfully() {
//        String creditCardId = "card-1";
//        User user = new User();
//        user.setId("user-1");
//        CreditCard creditCard = new CreditCard();
//        creditCard.setId(creditCardId);
//        creditCard.setUser(user);
//        TransactionCreateDTO dto = new TransactionCreateDTO(100.0, "Compra", "DEBIT");
//        Transaction transaction = new Transaction(dto.amount(), dto.description(), dto.type(), creditCard);
//        transaction.setId("tx-1");
//
//        when(creditCardRepository.findById(creditCardId)).thenReturn(Optional.of(creditCard));
//        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
//
//        TransactionResponseDTO response = useCase.execute(creditCardId, dto);
//
//        assertNotNull(response);
//        assertEquals("tx-1", response.id());
//        assertEquals(100.0, response.amount());
//        assertEquals("Compra", response.description());
//        assertEquals("DEBIT", response.type());
//        verify(notificationUseCase).execute(eq("user-1"), eq("tx-1"), eq(100.0), any());
//    }
//
//    @Test
//    void shouldThrowInvalidTransactionAmountException() {
//        String creditCardId = "card-1";
//        TransactionCreateDTO dto = new TransactionCreateDTO(0.0, "Compra", "DEBIT");
//        CreditCard creditCard = new CreditCard();
//        creditCard.setId(creditCardId);
//        User user = new User();
//        user.setId("user-1");
//        creditCard.setUser(user);
//        creditCard.setCreditLimit(new BigDecimal("1000.00"));
//        creditCard.setExpirationDate(LocalDate.now().plusMonths(1));
//
//        assertThrows(InvalidTransactionAmountException.class, () -> useCase.execute(creditCardId, dto));
//        // Não verificar findById, pois a validação do valor ocorre antes
//        verifyNoInteractions(creditCardRepository, transactionRepository, notificationUseCase);
//    }
//
//    @Test
//    void shouldThrowCreditCardNotFoundException() {
//        String creditCardId = "card-1";
//        TransactionCreateDTO dto = new TransactionCreateDTO(100.0, "Compra", "DEBIT");
//
//        when(creditCardRepository.findById(creditCardId)).thenReturn(Optional.empty());
//
//        assertThrows(CreditCardNotFoundException.class, () -> useCase.execute(creditCardId, dto));
//        verify(creditCardRepository).findById(creditCardId);
//        verifyNoInteractions(transactionRepository, notificationUseCase);
//    }
//}
