package ao.com.wundu.application.usercases.card;

import ao.com.wundu.application.dtos.card.*;
import ao.com.wundu.application.mappers.CreditCardMapper;
import ao.com.wundu.domain.entities.CreditCard;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.exceptions.*;
import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.service.EncryptionService;
import ao.com.wundu.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class AssociateCreditCardUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AssociateCreditCardUseCase.class);
    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final CreditCardMapper creditCardMapper;
    private final RestTemplate restTemplate;
    private final String bankingApiUrl;
    private final String bankingApiKey;
    private final int freePlanLimit;
    private final int premiumPlanLimit;

    public AssociateCreditCardUseCase(
            CreditCardRepository creditCardRepository,
            UserRepository userRepository,
            EncryptionService encryptionService,
            CreditCardMapper creditCardMapper,
            RestTemplate restTemplate,
            @Value("${wundu.banking.api.url}") String bankingApiUrl,
            @Value("${wundu.banking.api.key}") String bankingApiKey,
            @Value("${credit.card.limit.free:1}") int freePlanLimit,
            @Value("${credit.card.limit.premium:3}") int premiumPlanLimit
    ) {
        this.creditCardRepository = creditCardRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.creditCardMapper = creditCardMapper;
        this.restTemplate = restTemplate;
        this.bankingApiUrl = bankingApiUrl;
        this.bankingApiKey = bankingApiKey;
        this.freePlanLimit = freePlanLimit;
        this.premiumPlanLimit = premiumPlanLimit;
    }

    @Transactional
    public CreditCardResponseDTO execute(String userId, AssociateCreditCardRequestDTO dto) {
        logger.info("Iniciando associação de cartão para usuário: {}", userId);

        // Verificar usuário
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado: {}", userId);
                    return new UserNotFoundException("Usuário não encontrado");
                });

        // Verificar limite de cartões
        int currentCardCount = creditCardRepository.findByUserId(userId).size();
        String planType = user.getPlanType() != null ? user.getPlanType().toString() : "FREE";
        logger.debug("Plano do usuário: {}, Contagem atual de cartões: {}, Limite FREE: {}, Limite PREMIUM: {}",
                planType, currentCardCount, freePlanLimit, premiumPlanLimit);
        int maxCards = "PREMIUM".equalsIgnoreCase(planType) ? premiumPlanLimit : freePlanLimit;
        if (currentCardCount >= maxCards) {
            logger.error("Limite de cartões excedido para usuário: {}. Atual: {}, Máximo: {}",
                    userId, currentCardCount, maxCards);
            throw new CreditCardLimitExceededException("Limite de " + maxCards + " cartões atingido");
        }

        // Converter data MM/yy para LocalDate para enviar para Banking API
        LocalDate expirationDate;
        try {
            expirationDate = DateUtils.convertMMyyToLocalDate(dto.expirationDate());

            // Validar se a data está no futuro
            if (!DateUtils.isExpirationDateValid(expirationDate)) {
                throw new InvalidCardException("Data de expiração deve ser futura");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Erro na conversão da data de expiração: {}", e.getMessage());
            throw new InvalidCardException("Formato de data de expiração inválido. Use MM/yy");
        }

        // Chamar Wundu Banking API para validar cartão
        try {
            String url = bankingApiUrl + "/card/validate";
            CardValidateRequest bankingRequest = new CardValidateRequest(
                    dto.cardNumber(), null, expirationDate
                    );

            HttpEntity<CardValidateRequest> request = new HttpEntity<>(bankingRequest);
            logger.debug("Enviando requisição para Wundu Banking API: {}", url);
            ResponseEntity<CardValidateResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, CardValidateResponse.class
            );

            if (response.getBody() == null || !response.getBody().approved()) {
                logger.error("Cartão não aprovado pela Wundu Banking API: cardNumber=****{}",
                        dto.cardNumber().substring(12));
                throw new CardValidationException("Cartão não aprovado pelo banco");
            }

            Long externalCardId = response.getBody().cardId();
            String bankName = response.getBody().bankName();

            // Verificar se o cartão já está associado
            if (creditCardRepository.existsByExternalCardId(externalCardId.toString())) {
                logger.error("Cartão já associado: externalCardId={}", externalCardId);
                throw new CreditCardAlreadyAssociatedException("Cartão já associado");
            }

            // Criar entidade CreditCard
            CreditCard card = new CreditCard();
            card.setExternalCardId(externalCardId.toString());
            card.setUser(user);
            card.setCardNumber(encryptionService.encrypt(dto.cardNumber()));
            card.setBankName(bankName); // Usar bankName da resposta da Banking API
            card.setCardHolderName(dto.cardHolderName()); // Usar cardHolderName do request
            card.setExpirationDate(expirationDate);
            card = creditCardRepository.save(card);

            logger.info("Cartão associado com sucesso: externalCardId={}", externalCardId);
            return new CreditCardResponseDTO(
                    card.getId(),
                    maskCardNumber(dto.cardNumber()),
                    bankName,
                    card.getCardHolderName(),
                    card.getExpirationDate().toString(),
                    userId
            );
        } catch (HttpClientErrorException e) {
            logger.error("Erro ao chamar Wundu Banking API: {}", e.getMessage());
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new InvalidCardException("Dados do cartão inválidos");
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CreditCardNotFoundException("Cartão não encontrado no banco");
            }
            throw new RuntimeException("Erro ao integrar cartão", e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao associar cartão: {}", e.getMessage());
            throw new RuntimeException("Erro ao integrar cartão", e);
        }
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 16) {
            return "**** **** **** ****";
        }
        return "**** **** **** " + cardNumber.substring(12);
    }
}
