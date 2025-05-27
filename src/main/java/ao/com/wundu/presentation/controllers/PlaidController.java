package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeRequest;
import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeResponse;
import ao.com.wundu.application.dtos.plaid.PlaidLinkTokenResponse;
import ao.com.wundu.application.usercases.user.FindUserByIdUseCase;
import ao.com.wundu.infrastructure.services.PlaidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plaid")
public class PlaidController {

    private final PlaidService plaidService;
    private final FindUserByIdUseCase findUserByIdUseCase;

    public PlaidController(PlaidService plaidService, FindUserByIdUseCase findUserByIdUseCase) {
        this.plaidService = plaidService;
        this.findUserByIdUseCase = findUserByIdUseCase;
    }

    @Operation(summary = "Gerar link_token do Plaid", description = "Gera um link_token do Plaid para autenticar e conectar contas bancárias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token gerado com sucesso",
                    content = @Content(schema = @Schema(implementation = PlaidLinkTokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao gerar link_token", content = @Content)
    })
    @GetMapping("/link-token/user/{userId}")
    public ResponseEntity<PlaidLinkTokenResponse> createLinkToken(
            @Parameter(description = "ID do usuário", required = true) @PathVariable String userId) {
        System.out.println("🔔 Recebida requisição para gerar link_token para o usuário: " + userId);

        try {
            findUserByIdUseCase.execute(userId);
            PlaidLinkTokenResponse response = plaidService.createLinkToken(userId);
            System.out.println("✅ link_token gerado com sucesso: " + response.getLinkToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ Erro ao gerar link_token: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Trocar public_token do Plaid", description = "Troca um public_token do Plaid por um access_token e item_id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Troca realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = PlaidTokenExchangeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro na troca de token", content = @Content)
    })
    @PostMapping("/exchange-token")
    public ResponseEntity<PlaidTokenExchangeResponse> exchangeToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados contendo o public_token a ser trocado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PlaidTokenExchangeRequest.class)))
            @RequestBody PlaidTokenExchangeRequest request) {

        System.out.println("🔄 Recebida requisição para troca de public_token: " + request.getPublicToken());

        try {
            PlaidTokenExchangeResponse response = plaidService.exchangePublicToken(request);
            System.out.println("✅ Token trocado com sucesso. Item ID: " + response.getItemId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ Erro na troca de token: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
