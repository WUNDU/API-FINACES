package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeRequest;
import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeResponse;
import ao.com.wundu.application.dtos.plaid.PlaidLinkTokenResponse;
import ao.com.wundu.infrastructure.services.PlaidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plaid")
public class PlaidController {

    private final PlaidService plaidService;

    public PlaidController(PlaidService plaidService) {
        this.plaidService = plaidService;
    }

    @GetMapping("/link-token/user/{userId}")
    public ResponseEntity<PlaidLinkTokenResponse> createLinkToken(@PathVariable String userId) {
        System.out.println("🔔 Recebida requisição para gerar link_token para o usuário: " + userId);

        try {
            PlaidLinkTokenResponse response = plaidService.createLinkToken(userId);
            System.out.println("✅ link_token gerado com sucesso: " + response.getLinkToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ Erro ao gerar link_token: " + e.getMessage());
            return ResponseEntity.badRequest().build();
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