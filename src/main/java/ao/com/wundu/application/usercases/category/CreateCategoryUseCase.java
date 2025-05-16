package ao.com.wundu.application.usercases.category;

import ao.com.wundu.application.dtos.category.CategoryCreateDTO;
import ao.com.wundu.application.dtos.category.CategoryResponseDTO;
import ao.com.wundu.domain.entities.Category;
import ao.com.wundu.domain.entities.Transaction;
import ao.com.wundu.domain.exceptions.TransactionNotFoundException;
import ao.com.wundu.infrastructure.repositories.CategoryRepository;
import ao.com.wundu.infrastructure.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateCategoryUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateCategoryUseCase.class);

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public CreateCategoryUseCase(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public CategoryResponseDTO execute(String transactionId, CategoryCreateDTO dto) {
        logger.info("Criando categoria para transação: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    logger.error("Transação não encontrada: {}", transactionId);
                    return new TransactionNotFoundException("Transação não encontrada");
                });

        Category category = new Category(dto.name(), dto.description(), transaction);
        category = categoryRepository.save(category);
        transaction.setCategory(category);
        transactionRepository.save(transaction);

        logger.info("Categoria criada com sucesso: {}", category.getId());
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                transaction.getId()
        );
    }
}
