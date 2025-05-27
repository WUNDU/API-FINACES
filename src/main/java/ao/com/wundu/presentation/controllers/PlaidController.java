package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeRequest;
import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeResponse;
import ao.com.wundu.application.dtos.plaid.PlaidLinkTokenResponse;
import ao.com.wundu.application.usercases.user.FindUserByIdUseCase;
import ao.com.wundu.infrastructure.services.PlaidService;
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

    @GetMapping("/link-token/user/{userId}")
    public ResponseEntity<PlaidLinkTokenResponse> createLinkToken(@PathVariable String userId) {
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

    @PostMapping("/exchange-token")
    public ResponseEntity<PlaidTokenExchangeResponse> exchangeToken(@RequestBody PlaidTokenExchangeRequest request) {
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
