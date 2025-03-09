package ao.com.wundu.controller;

import ao.com.wundu.dto.CreditCardCreateDTO;
import ao.com.wundu.dto.CreditCardResponseDTO;
import ao.com.wundu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit-cards")
@Tag(name = "Credit Cards", description = "Operations related to user credit cards")
public class CreditCardController {

    @Autowired
    private UserService  userService;

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
        CreditCardResponseDTO response = userService.addCreditCard(userId, create);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
