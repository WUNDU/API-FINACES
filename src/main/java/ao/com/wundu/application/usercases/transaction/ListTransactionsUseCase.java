package ao.com.wundu.application.usercases.transaction;

import ao.com.wundu.application.dtos.transation.TransactionEventDTO;
import ao.com.wundu.application.dtos.transation.TransactionResponseDTO;
import ao.com.wundu.domain.entities.CreditCard;
import ao.com.wundu.domain.exceptions.CreditCardNotFoundException;
import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
import ao.com.wundu.infrastructure.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListTransactionsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ListTransactionsUseCase.class);

    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;
    private final SyncTransactionsFromBankingUseCase syncTransactionsUseCase;
    private final ProcessTransactionEventUseCase processTransactionUseCase;

    public ListTransactionsUseCase(
            CreditCardRepository creditCardRepository,
            TransactionRepository transactionRepository,
            SyncTransactionsFromBankingUseCase syncTransactionsUseCase,
            ProcessTransactionEventUseCase processTransactionUseCase
    ) {
        this.creditCardRepository = creditCardRepository;
        this.transactionRepository = transactionRepository;
        this.syncTransactionsUseCase = syncTransactionsUseCase;
        this.processTransactionUseCase = processTransactionUseCase;
    }

    @Transactional(readOnly = false)
    public List<TransactionResponseDTO> execute(String creditCardId, String userId) {
        logger.info("Buscando transações para cartão: {}, usuário: {}", creditCardId, userId);

        CreditCard creditCard = creditCardRepository.findById(creditCardId)
                .orElseThrow(() -> {
                    logger.error("Cartão não encontrado: {}", creditCardId);
                    return new CreditCardNotFoundException("Cartão não encontrado");
                });

//        if (!creditCard.getUser().getId().equals(userId)) {
//            logger.error("Usuário {} não autorizado para acessar cartão {}", userId, creditCardId);
//            throw new UnauthorizedAccessException("Acesso não autorizado a este cartão");
//        }

        // Sincronizar transações da Wundu Banking API
        List<TransactionEventDTO> bankingTransactions = syncTransactionsUseCase.execute(creditCardId, userId);
        for (TransactionEventDTO event : bankingTransactions) {
            try {
                processTransactionUseCase.execute(event);
            } catch (Exception e) {
                logger.warn("Erro ao processar transação {}: {}", event.externalCardId(), e.getMessage());
            }
        }

        // Listar transações locais
        List<TransactionResponseDTO> transactions = transactionRepository.findByCreditCardId(creditCardId)
                .stream()
                .map(transaction -> new TransactionResponseDTO(
                        transaction.id(),
                        transaction.amount(),
                        transaction.description(),
                        transaction.type(),
                        creditCardId,
                        transaction.dateTime(),
                        transaction.categoryId() != null ? transaction.categoryId() : null
                ))
                .collect(Collectors.toList());

        logger.info("Encontradas {} transações para cartão: {}", transactions.size(), creditCardId);
        return transactions;
    }
}
