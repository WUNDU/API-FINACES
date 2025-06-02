package ao.com.wundu.application.usercases.transaction;

import ao.com.wundu.application.dtos.transation.TransactionEventDTO;
import ao.com.wundu.application.usercases.notification.SendTransactionNotificationUseCase;
import ao.com.wundu.domain.entities.CreditCard;
import ao.com.wundu.domain.entities.Transaction;
import ao.com.wundu.domain.exceptions.CreditCardNotFoundException;
import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
import ao.com.wundu.infrastructure.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ProcessTransactionEventUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ProcessTransactionEventUseCase.class);

    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;
    private final SendTransactionNotificationUseCase notificationUseCase;

    public ProcessTransactionEventUseCase(
            CreditCardRepository creditCardRepository,
            TransactionRepository transactionRepository,
            SendTransactionNotificationUseCase notificationUseCase
    ) {
        this.creditCardRepository = creditCardRepository;
        this.transactionRepository = transactionRepository;
        this.notificationUseCase = notificationUseCase;
    }

    @RabbitListener(queues = "transaction-queue")
    public void execute(TransactionEventDTO event) {
        logger.info("Processando evento de transação: externalCardId={}", event.externalCardId());

        CreditCard card = creditCardRepository.findByExternalCardId(event.externalCardId().toString())
                .orElseThrow(() -> {
                    logger.error("Cartão não encontrado: externalCardId={}", event.externalCardId());
                    return new CreditCardNotFoundException("Cartão não encontrado");
                });

        Transaction transaction = new Transaction(
                event.amount(),
                event.description(),
                event.type(),
                card
        );
        transaction.setDateTime(event.dateTime());
        transaction = transactionRepository.save(transaction);

        try {
            notificationUseCase.execute(
                    card.getUser().getId(),
                    transaction.getId(),
                    event.amount(),
                    event.dateTime()
            );
            logger.info("Notificação enviada para transação: {}", transaction.getId());
        } catch (Exception e) {
            logger.error("Falha ao enviar notificação para transação: {}", transaction.getId(), e);
        }
    }
}
