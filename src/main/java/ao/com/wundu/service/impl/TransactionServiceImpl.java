package ao.com.wundu.service.impl;

import ao.com.wundu.dto.*;
import ao.com.wundu.entity.Transaction;
import ao.com.wundu.exception.CreditCardNotFoundException;
import ao.com.wundu.exception.InvalidTransactionAmountException;
import ao.com.wundu.exception.TransactionNotFoundException;
import ao.com.wundu.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ao.com.wundu.service.TransactionService;
import ao.com.wundu.entity.CreditCard;
import ao.com.wundu.repository.CreditCardRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Override
    public TransactionResponseDTO createTransaction(String creditCardId, TransactionCreateDTO create) {
        CreditCard creditCard = creditCardRepository.findById(creditCardId)
                .orElseThrow(() -> new CreditCardNotFoundException("Cartão de crédito não encontrado"));
        if (create.amount() <= 0) {
            throw new InvalidTransactionAmountException("Valor da transação deve ser positivo");
        }
        Transaction transaction = new Transaction(create.amount(), create.description(), create.type(), creditCard);
        transaction = transactionRepository.save(transaction);
        // creditCard.getTransactions().add(transaction); Ou deixa assim
        creditCardRepository.save(creditCard);
        return new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDescription(),
                transaction.getType(), creditCardId, transaction.getDateTime().toString());
    }

    @Override
    public TransactionResponseDTO findTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transação não encontrada"));
        return new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDescription(),
                transaction.getType(), transaction.getCreditCard().getId(), transaction.getDateTime().toString());
    }

    @Override
    public List<TransactionResponseDTO> findAllTransactions() {
        return transactionRepository.findAll().stream().map(t -> new TransactionResponseDTO(t.getId(), t.getAmount(),
                t.getDescription(), t.getType(), t.getCreditCard().getId(), t.getDateTime().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTransaction(String id) {
        if (!transactionRepository.existsById(id))
            throw new TransactionNotFoundException("Transação não encontrada");
        transactionRepository.deleteById(id);
    }
}
