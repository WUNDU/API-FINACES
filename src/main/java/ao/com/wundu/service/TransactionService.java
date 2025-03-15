package ao.com.wundu.service;

import ao.com.wundu.dto.*;
import java.util.List;

public interface TransactionService {
    TransactionResponseDTO createTransaction(TransactionCreateDTO create);

    TransactionResponseDTO updateTransaction(String id, TransactionUpdateDTO update);

    TransactionResponseDTO findTransactionById(String id);

    List<TransactionResponseDTO> findAllTransactions();

    void deleteTransaction(String id);
}
