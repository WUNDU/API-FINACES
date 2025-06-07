package ao.com.wundu.application.usercases.transaction;

import ao.com.wundu.application.dtos.transation.TransactionEventDTO;
import ao.com.wundu.domain.entities.CreditCard;
import ao.com.wundu.domain.exceptions.CreditCardNotFoundException;
import ao.com.wundu.domain.exceptions.UnauthorizedAccessException;
import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class SyncTransactionsFromBankingUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SyncTransactionsFromBankingUseCase.class);

    private final CreditCardRepository creditCardRepository;
    private final RestTemplate restTemplate;
    private final String bankingApiUrl;

    public SyncTransactionsFromBankingUseCase(
            CreditCardRepository creditCardRepository,
            RestTemplate restTemplate,
            @Value("${wundu.banking.api.url}") String bankingApiUrl
    ) {
        this.creditCardRepository = creditCardRepository;
        this.restTemplate = restTemplate;
        this.bankingApiUrl = bankingApiUrl;
    }

    public List<TransactionEventDTO> execute(String creditCardId, String userId) {
        logger.info("Sincronizando transações para cartão: {}, usuário: {}", creditCardId, userId);

        CreditCard creditCard = creditCardRepository.findById(creditCardId)
                .orElseThrow(() -> {
                    logger.error("Cartão não encontrado: {}", creditCardId);
                    return new CreditCardNotFoundException("Cartão não encontrado");
                });

        String url = bankingApiUrl + "/transactions/card/" + creditCard.getExternalCardId() + "/transactions";
        logger.debug("Chamando Wundu Banking API: {}", url);
        HttpEntity<?> request = new HttpEntity<>(new HttpHeaders());
        try {
            ResponseEntity<TransactionEventDTO[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, TransactionEventDTO[].class
            );

            if (response.getBody() == null) {
                logger.warn("Nenhuma transação encontrada para cartão: {}", creditCardId);
                return List.of();
            }

            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            logger.error("Erro ao sincronizar transações da Wundu Banking API: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao sincronizar transações", e);
        }
    }
}
