package ao.com.wundu.application.usercases.card;

import ao.com.wundu.application.dtos.card.CreditCardCreateDTO;
import ao.com.wundu.application.dtos.card.CreditCardResponseDTO;
import ao.com.wundu.application.mappers.CreditCardMapper;
import ao.com.wundu.domain.entities.CreditCard;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.exceptions.CreditCardLimitExceededException;
import ao.com.wundu.domain.exceptions.InvalidCardException;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.service.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AddCreditCardUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AddCreditCardUseCase.class);

    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final CreditCardMapper creditCardMapper;
    private final int freePlanLimit;
    private final int premiumPlanLimit;

    public AddCreditCardUseCase(
            CreditCardRepository creditCardRepository,
            UserRepository userRepository,
            EncryptionService encryptionService,
            CreditCardMapper creditCardMapper,
            @Value("${credit.card.limit.free:1}") int freePlanLimit,
            @Value("${credit.card.limit.premium:3}") int premiumPlanLimit
    ) {
        this.creditCardRepository = creditCardRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.creditCardMapper = creditCardMapper;
        this.freePlanLimit = freePlanLimit;
        this.premiumPlanLimit = premiumPlanLimit;
    }

    public CreditCardResponseDTO execute(String userId, CreditCardCreateDTO dto) {
        logger.info("Iniciando adição de cartão para usuário: {}", userId);

        // Validar DTO
        if (dto == null || dto.cardNumber() == null || dto.bankName() == null || dto.creditLimit() == null || dto.expirationDate() == null) {
            logger.error("CreditCardCreateDTO contém campos nulos: {}", dto);
            throw new IllegalArgumentException("Todos os campos do cartão são obrigatórios");
        }

        // Validar cartão
        if (!CreditCard.validateCardNumber(dto.cardNumber())) {
            logger.error("Número de cartão inválido: {}", dto.cardNumber());
            throw new InvalidCardException("Número de cartão inválido");
        }
        logger.debug("Data de expiração recebida: {}, Data atual: {}", dto.expirationDate(), LocalDate.now());
        boolean isValidExpiration = CreditCard.validateExpirationDate(dto.expirationDate());
        logger.debug("Validação de data de expiração: {}", isValidExpiration);
        if (!isValidExpiration) {
            logger.error("Data de expiração inválida: {}", dto.expirationDate());
            throw new InvalidCardException("Data de expiração deve ser futura");
        }

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
            logger.error("Limite de cartões excedido para usuário: {}. Atual: {}, Máximo: {}", userId, currentCardCount, maxCards);
            throw new CreditCardLimitExceededException("Limite de " + maxCards + " cartões atingido");
        }

        // Mapear DTO para entidade com número original
        CreditCard card;
        try {
            card = creditCardMapper.toEntity(dto);
            String originalCardNumber = dto.cardNumber();
            card.setCardNumber(originalCardNumber); // Define o número original para extrair lastFourDigits
            logger.debug("LastFourDigits após setCardNumber com número original: {}", card.getLastFourDigits());
        } catch (Exception e) {
            logger.error("Erro ao mapear CreditCardCreateDTO para entidade: {}", dto, e);
            throw new RuntimeException("Erro ao mapear dados do cartão", e);
        }
        card.setUser(user);

        // Criptografar número do cartão
        String encryptedCardNumber;
        try {
            encryptedCardNumber = encryptionService.encrypt(dto.cardNumber());
        } catch (Exception e) {
            logger.error("Erro ao criptografar número do cartão para usuário: {}", userId, e);
            throw new RuntimeException("Erro ao criptografar número do cartão", e);
        }

        // Salvar cartão
        try {
            card = creditCardRepository.save(card);
            logger.info("Cartão adicionado com sucesso para usuário: {}", userId);
        } catch (Exception e) {
            logger.error("Erro ao salvar cartão no banco para usuário: {}", userId, e);
            throw new RuntimeException("Erro ao salvar cartão no banco de dados", e);
        }

        // Mapear para DTO de resposta
        try {
            return creditCardMapper.toResponseDTO(card);
        } catch (Exception e) {
            logger.error("Erro ao mapear CreditCard para CreditCardResponseDTO: {}", card, e);
            throw new RuntimeException("Erro ao gerar resposta do cartão", e);
        }
    }
}
