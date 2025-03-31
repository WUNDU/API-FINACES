package ao.com.wundu.controller;

import ao.com.wundu.dto.CreditCardCreateDTO;
import ao.com.wundu.dto.CreditCardResponseDTO;
import ao.com.wundu.service.CreditCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credit-cards")
@Tag(name = "Credit Cards", description = "Operations related to user credit cards")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

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
        CreditCardResponseDTO response = creditCardService.addCreditCard(userId, create);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List credit cards by user", description = "Retrieves all credit cards linked to a user")
    @ApiResponse(responseCode = "200", description = "Credit card retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Credit card not found")
    public ResponseEntity<List<CreditCardResponseDTO>> getCreditCardsByUser(
            @Parameter(description = "User ID", required = true, example = "b28906bf-561b-4b61-9bd4-fd7c23eec23f")
            @PathVariable String userId) {

        List<CreditCardResponseDTO> response = creditCardService.getCreditCardsByUser(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get credit card details", description = "Retrieves details of a specific credit card")
    @ApiResponse(responseCode = "200", description = "Credit card retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Credit card not found")
    public ResponseEntity<CreditCardResponseDTO> getCreditCard(
            @Parameter(description = "Credit Card ID", required = true, example = "b28906bf-561b-4b61-9bd4-fd7c23eec23f")
            @PathVariable String id) {
        CreditCardResponseDTO response = creditCardService.getCreaditCard(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
