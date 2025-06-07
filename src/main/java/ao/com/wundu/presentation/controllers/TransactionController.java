package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.category.CategoryAssignDTO;
import ao.com.wundu.application.dtos.transation.TransactionResponseDTO;
import ao.com.wundu.application.usercases.transaction.CategorizeTransactionUseCase;
import ao.com.wundu.application.usercases.transaction.ListTransactionsByCategoryUseCase;
import ao.com.wundu.application.usercases.transaction.ListTransactionsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions/")
@Tag(name = "Transactions", description = "Operations related to financial transactions")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final CategorizeTransactionUseCase categorizeTransactionUseCase;
    private final ListTransactionsUseCase listTransactionsUseCase;
    private final ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;

    public TransactionController(ListTransactionsUseCase listTransactionsUseCase,
                                 CategorizeTransactionUseCase categorizeTransactionUseCase,
                                 ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase
                                 ) {
        this.listTransactionsUseCase = listTransactionsUseCase;
        this.categorizeTransactionUseCase = categorizeTransactionUseCase;
        this.listTransactionsByCategoryUseCase = listTransactionsByCategoryUseCase;
    }

    @GetMapping("/credit-card/{creditCardId}")
    @Operation(summary = "List transactions for a credit card", description = "Retrieves all transactions associated with a credit card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Credit card not found")
    })
    public ResponseEntity<List<TransactionResponseDTO>> listTransactions(
            @Parameter(description = "Credit card ID", required = true, example = "dba9b87d-c222-4045-aa18-e487a449a877")
            @PathVariable String creditCardId) {
        logger.info("Listando transações para cartão: {}", creditCardId);
        List<TransactionResponseDTO> transactions = listTransactionsUseCase.execute(creditCardId, null);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{transactionId}/category")
    @Operation(summary = "Categorize a transaction", description = "Assigns a category to a specific transaction")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction categorized successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction or category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<TransactionResponseDTO> categorizeTransaction(
            @Parameter(description = "Transaction ID", required = true, example = "6bcc164c-3a88-4ef4-91a0-ce99aef83120")
            @PathVariable String transactionId,
            @Valid @RequestBody CategoryAssignDTO dto) {
        logger.info("Recebida requisição para categorizar transação: {}", transactionId);
        TransactionResponseDTO response = categorizeTransactionUseCase.execute(transactionId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "List transactions by category", description = "Retrieves all transactions associated with a specific category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<List<TransactionResponseDTO>> listTransactionsByCategory(
            @Parameter(description = "Category ID", required = true, example = "cat1")
            @PathVariable String categoryId) {
        logger.info("Recebida requisição para listar transações por categoria: {}", categoryId);
        List<TransactionResponseDTO> transactions = listTransactionsByCategoryUseCase.execute(categoryId);
        return ResponseEntity.ok(transactions);
    }
}
