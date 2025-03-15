package ao.com.wundu.service.impl;

import ao.com.wundu.dto.*;
import ao.com.wundu.entity.Transaction;
import ao.com.wundu.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import ao.com.wundu.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public TransactionResponseDTO createTransaction(TransactionCreateDTO create) {
        if (create.amount() <= 0)
            throw new IllegalArgumentException("Valor da transação deve ser positivo");
        Transaction transaction = new Transaction(create.amount(), create.description(), create.type(),
                create.accountId());
        transaction = transactionRepository.save(transaction);
        return new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDescription(),
                transaction.getType(), transaction.getAccountId(), transaction.getDateTime().toString());
    }

    @Override
    public TransactionResponseDTO updateTransaction(String id, TransactionUpdateDTO update) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));
        if (update.amount() != null && update.amount() <= 0)
            throw new IllegalArgumentException("Valor da transação deve ser positivo");
        if (update.amount() != null)
            transaction.setAmount(update.amount());
        if (update.description() != null)
            transaction.setDescription(update.description());
        if (update.type() != null)
            transaction.setType(update.type());
        transaction = transactionRepository.save(transaction);
        return new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDescription(),
                transaction.getType(), transaction.getAccountId(), transaction.getDateTime().toString());
    }

    @Override
    public TransactionResponseDTO findTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));
        return new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDescription(),
                transaction.getType(), transaction.getAccountId(), transaction.getDateTime().toString());
    }

    @Override
    public List<TransactionResponseDTO> findAllTransactions() {
        return transactionRepository.findAll().stream().map(t -> new TransactionResponseDTO(t.getId(), t.getAmount(),
                t.getDescription(), t.getType(), t.getAccountId(), t.getDateTime().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTransaction(String id) {
        if (!transactionRepository.existsById(id))
            throw new IllegalArgumentException("Transação não encontrada");
        transactionRepository.deleteById(id);
    }
}
