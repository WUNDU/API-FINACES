//package ao.com.wundu.unit.category;
//
//import ao.com.wundu.application.dtos.category.CategoryCreateDTO;
//import ao.com.wundu.application.dtos.category.CategoryResponseDTO;
//import ao.com.wundu.application.usercases.category.CreateCategoryUseCase;
//import ao.com.wundu.domain.entities.Category;
//import ao.com.wundu.domain.entities.Transaction;
//import ao.com.wundu.domain.exceptions.TransactionNotFoundException;
//import ao.com.wundu.infrastructure.repositories.CategoryRepository;
//import ao.com.wundu.infrastructure.repositories.TransactionRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CreateCategoryUseCaseTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @InjectMocks
//    private CreateCategoryUseCase useCase;
//
//    @Test
//    void shouldCreateCategorySuccessfully() {
//        String transactionId = "tx-1";
//        Transaction transaction = new Transaction();
//        transaction.setId(transactionId);
//        CategoryCreateDTO dto = new CategoryCreateDTO("Compra", "Supermercado");
//        Category category = new Category(dto.name(), dto.description(), transaction);
//        category.setId("cat-1");
//
//        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
//        when(categoryRepository.save(any(Category.class))).thenReturn(category);
//
//        CategoryResponseDTO response = useCase.execute(transactionId, dto);
//
//        assertNotNull(response);
//        assertEquals("cat-1", response.id());
//        assertEquals("Compra", response.name());
//        assertEquals("Supermercado", response.description());
//        assertEquals(transactionId, response.transactionId());
//        verify(transactionRepository).save(transaction);
//    }
//
//    @Test
//    void shouldThrowTransactionNotFoundException() {
//        String transactionId = "tx-1";
//        CategoryCreateDTO dto = new CategoryCreateDTO("Compra", "Supermercado");
//
//        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());
//
//        assertThrows(TransactionNotFoundException.class, () -> useCase.execute(transactionId, dto));
//    }
//
//}
