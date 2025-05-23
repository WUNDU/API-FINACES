package ao.com.wundu.infrastructure.services.impl;

import ao.com.wundu.application.dtos.plaid.PlaidLinkTokenResponse;
import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeRequest;
import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeResponse;
import ao.com.wundu.infrastructure.services.PlaidService;
import ao.com.wundu.service.EncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlaidServiceImpl implements PlaidService {

    @Value("${plaid.client-id}")
    private String clientId;

    @Value("${plaid.secret}")
    private String secret;

    @Value("${plaid.env}")
    private String env;

    private final EncryptionService encryptionService;
    private final RestTemplate restTemplate = new RestTemplate();

    public PlaidServiceImpl(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public PlaidTokenExchangeResponse exchangePublicToken(PlaidTokenExchangeRequest request) throws Exception {
        String url = "https://sandbox.plaid.com/item/public_token/exchange";
        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("secret", secret);
        body.put("public_token", request.getPublicToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        String accessToken = (String) response.getBody().get("access_token");
        String itemId = (String) response.getBody().get("item_id");

        String encryptedToken = encryptionService.encrypt(accessToken);

        return new PlaidTokenExchangeResponse(encryptedToken, itemId);
    }

    @Override
    public PlaidLinkTokenResponse createLinkToken(String userId) throws Exception {
        String url = "https://sandbox.plaid.com/link/token/create";

        Map<String, Object> user = new HashMap<>();
        user.put("client_user_id", userId);

        Map<String, Object> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("secret", secret);
        body.put("client_name", "Wundu Finance App");
        body.put("country_codes", new String[]{"US"});
        body.put("language", "en");
        body.put("user", user);
        body.put("products", new String[]{"auth", "transactions"});

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        String linkToken = (String) response.getBody().get("link_token");
        return new PlaidLinkTokenResponse(linkToken);
    }

}
