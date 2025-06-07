package ao.com.wundu.application.usercases.transaction;

import ao.com.wundu.application.dtos.category.CategoryAssignDTO;
import ao.com.wundu.application.dtos.transation.TransactionResponseDTO;
import ao.com.wundu.domain.entities.Category;
import ao.com.wundu.domain.entities.Transaction;
import ao.com.wundu.domain.exceptions.CategoryNotFoundException;
import ao.com.wundu.domain.exceptions.TransactionNotFoundException;
import ao.com.wundu.infrastructure.repositories.CategoryRepository;
import ao.com.wundu.infrastructure.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategorizeTransactionUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CategorizeTransactionUseCase.class);

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public CategorizeTransactionUseCase(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public TransactionResponseDTO execute(String transactionId, CategoryAssignDTO dto) {
        logger.info("Categorizando transação: transactionId={}, categoryId={}", transactionId, dto.categoryId());

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    logger.error("Transação não encontrada: {}", transactionId);
                    return new TransactionNotFoundException("Transação não encontrada");
                });

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> {
                    logger.error("Categoria não encontrada: {}", dto.categoryId());
                    return new CategoryNotFoundException("Categoria não encontrada");
                });

        transaction.setCategory(category);
        transactionRepository.save(transaction);

        logger.info("Transação categorizada com sucesso: transactionId={}, categoryId={}", transactionId, dto.categoryId());
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getType(),
                transaction.getCreditCard().getId(),
                transaction.getDateTime(),
                category.getId()
        );
    }
}
