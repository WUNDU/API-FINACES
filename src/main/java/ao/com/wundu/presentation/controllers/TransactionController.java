//package ao.com.wundu.presentation.controllers;
//
//import ao.com.wundu.application.dtos.transation.TransactionCreateDTO;
//import ao.com.wundu.application.dtos.transation.TransactionResponseDTO;
//import ao.com.wundu.application.usercases.transaction.CreateTransactionUseCase;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import jakarta.validation.Valid;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/transactions")
//public class TransactionController {
//
//    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
//
//    private final CreateTransactionUseCase createTransactionUseCase;
//
//    public TransactionController(CreateTransactionUseCase createTransactionUseCase) {
//        this.createTransactionUseCase = createTransactionUseCase;
//    }
//
//    @PostMapping("/credit-card/{creditCardId}")
//    @Operation(summary = "Create a transaction for a credit card")
//    @ApiResponse(responseCode = "201", description = "Transaction created successfully")
//    public ResponseEntity<TransactionResponseDTO> createTransaction(
//            @PathVariable String creditCardId,
//            @Valid @RequestBody TransactionCreateDTO dto
//    ) {
//        logger.info("Criando transação para cartão: {}", creditCardId);
//        TransactionResponseDTO response = createTransactionUseCase.execute(creditCardId, dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//}
