package ao.com.wundu.application.usercases.transaction;

import ao.com.wundu.application.dtos.transation.TransactionResponseDTO;
import ao.com.wundu.domain.entities.Category;
import ao.com.wundu.domain.entities.Transaction;
import ao.com.wundu.domain.exceptions.CategoryNotFoundException;
import ao.com.wundu.infrastructure.repositories.CategoryRepository;
import ao.com.wundu.infrastructure.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListTransactionsByCategoryUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ListTransactionsByCategoryUseCase.class);

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public ListTransactionsByCategoryUseCase(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> execute(String categoryId) {
        logger.info("Listando transações para categoria: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.error("Categoria não encontrada: {}", categoryId);
                    return new CategoryNotFoundException("Categoria não encontrada");
                });

        List<Transaction> transactions = transactionRepository.findByCategoryId(categoryId);
        logger.debug("Encontradas {} transações para categoria: {}", transactions.size(), categoryId);

        return transactions.stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getAmount(),
                        t.getDescription(),
                        t.getType(),
                        t.getCreditCard().getId(),
                        t.getDateTime(),
                        categoryId
                ))
                .collect(Collectors.toList());
    }
}
