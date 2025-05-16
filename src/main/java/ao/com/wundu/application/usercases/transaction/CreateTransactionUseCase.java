package ao.com.wundu.application.usercases.transaction;

import ao.com.wundu.application.dtos.transation.TransactionCreateDTO;
import ao.com.wundu.application.dtos.transation.TransactionResponseDTO;
import ao.com.wundu.application.usercases.notification.SendTransactionNotificationUseCase;
import ao.com.wundu.domain.entities.CreditCard;
import ao.com.wundu.domain.entities.Transaction;
import ao.com.wundu.domain.exceptions.CreditCardNotFoundException;
import ao.com.wundu.domain.exceptions.InvalidTransactionAmountException;
import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
import ao.com.wundu.infrastructure.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTransactionUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateTransactionUseCase.class);

    private final TransactionRepository transactionRepository;
    private final CreditCardRepository creditCardRepository;
    private final SendTransactionNotificationUseCase notificationUseCase;

    public CreateTransactionUseCase(
            TransactionRepository transactionRepository,
            CreditCardRepository creditCardRepository,
            SendTransactionNotificationUseCase notificationUseCase
    ) {
        this.transactionRepository = transactionRepository;
        this.creditCardRepository = creditCardRepository;
        this.notificationUseCase = notificationUseCase;
    }

    @Transactional
    public TransactionResponseDTO execute(String creditCardId, TransactionCreateDTO dto) {
        logger.info("Criando transação para cartão: {}", creditCardId);

        if (dto.amount() <= 0.0) {
            logger.error("Valor da transação inválido: {}", dto.amount());
            throw new InvalidTransactionAmountException("Valor da transação deve ser positivo");
        }

        CreditCard creditCard = creditCardRepository.findById(creditCardId)
                .orElseThrow(() -> {
                    logger.error("Cartão não encontrado: {}", creditCardId);
                    return new CreditCardNotFoundException("Cartão de crédito não encontrado");
                });

        Transaction transaction = new Transaction(dto.amount(), dto.description(), dto.type(), creditCard);
        transaction = transactionRepository.save(transaction);

        // Enviar notificação
        try {
            notificationUseCase.execute(creditCard.getUser().getId(), transaction.getId(), dto.amount(), transaction.getDateTime());
        } catch (Exception e) {
            logger.error("Falha ao enviar notificação para transação: {}", transaction.getId(), e);
            // Não falha a transação, apenas loga o erro
        }

        logger.info("Transação criada com sucesso: {}", transaction.getId());
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getType(),
                creditCard.getId(),
                transaction.getDateTime().toString(),
                transaction.getCategory() != null ? transaction.getCategory().getId() : null
        );
    }
}
