package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.card.CreditCardCreateDTO;
import ao.com.wundu.application.dtos.card.CreditCardResponseDTO;
import ao.com.wundu.application.usercases.card.AddCreditCardUseCase;
import ao.com.wundu.application.usercases.card.ListCreditCardsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credit-cards")
@Tag(name = "Credit Cards", description = "Operations related to user credit cards")
public class CreditCardController {

    private static final Logger logger = LoggerFactory.getLogger(CreditCardController.class);

    private final AddCreditCardUseCase addCreditCardUseCase;
    private final ListCreditCardsUseCase listCreditCardsUseCase;

    public CreditCardController(AddCreditCardUseCase addCreditCardUseCase, ListCreditCardsUseCase listCreditCardsUseCase) {
        this.addCreditCardUseCase = addCreditCardUseCase;
        this.listCreditCardsUseCase = listCreditCardsUseCase;
    }

    @PostMapping("/user/{userId}")
    @Operation(summary = "Link a credit card to a user", description = "adds a credit card to an existing user")
    @ApiResponse(responseCode = "200", description = "Credit card linked successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or limit reached")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<CreditCardResponseDTO> addCreditCard(
            @Parameter(description = "User ID", required = true, example = "b28906bf-561b-4b61-9bd4-fd7c23eec23f")
            @PathVariable String userId,
            @Parameter(description = "Credit Card data for creation", required = true)
            @Valid @RequestBody CreditCardCreateDTO create
            ) {
        logger.info("Recebida solicitação para adicionar cartão para usuário: {}", userId);
        CreditCardResponseDTO response = addCreditCardUseCase.execute(userId, create);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List credit cards by user", description = "Retrieves all credit cards linked to a user")
    @ApiResponse(responseCode = "200", description = "Credit card retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Credit card not found")
    public ResponseEntity<List<CreditCardResponseDTO>> getCreditCardsByUser(
            @Parameter(description = "User ID", required = true, example = "b28906bf-561b-4b61-9bd4-fd7c23eec23f")
            @PathVariable String userId) {

        List<CreditCardResponseDTO> response = listCreditCardsUseCase.execute(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

//    @GetMapping("/{id}")
//    @Operation(summary = "Get credit card details", description = "Retrieves details of a specific credit card")
//    @ApiResponse(responseCode = "200", description = "Credit card retrieved successfully")
//    @ApiResponse(responseCode = "404", description = "Credit card not found")
//    public ResponseEntity<CreditCardResponseDTO> getCreditCard(
//            @Parameter(description = "Credit Card ID", required = true, example = "b28906bf-561b-4b61-9bd4-fd7c23eec23f")
//            @PathVariable String userId) {
//        logger.info("Recebida solicitação para listar cartões do usuário: {}", userId);
//        CreditCardResponseDTO response = creditCardService.getCreaditCard(userId);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(response);
//    }
}
