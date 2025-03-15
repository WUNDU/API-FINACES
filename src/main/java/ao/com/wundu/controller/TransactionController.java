package ao.com.wundu.controller;

import ao.com.wundu.dto.TransactionCreateDTO;
import ao.com.wundu.dto.TransactionResponseDTO;
import ao.com.wundu.dto.TransactionUpdateDTO;
import ao.com.wundu.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionCreateDTO create) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(create));
    }

    //@PutMapping("/{id}")
    //public ResponseEntity<TransactionResponseDTO> updateTransaction(@PathVariable String id,
    //        @RequestBody TransactionUpdateDTO update) {
    //    return ResponseEntity.ok(transactionService.updateTransaction(id, update));
    //}

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> findTransactionById(@PathVariable String id) {
        return ResponseEntity.ok(transactionService.findTransactionById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> findAllTransactions() {
        return ResponseEntity.ok(transactionService.findAllTransactions());
    }

    //@DeleteMapping("/{id}")
    //public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
    //    transactionService.deleteTransaction(id);
    //    return ResponseEntity.noContent().build();
    //}
}
