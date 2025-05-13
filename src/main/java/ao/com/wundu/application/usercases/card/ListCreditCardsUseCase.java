package ao.com.wundu.application.usercases.card;

import ao.com.wundu.application.dtos.card.CreditCardResponseDTO;
import ao.com.wundu.application.mappers.CreditCardMapper;
import ao.com.wundu.domain.entities.CreditCard;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.CreditCardRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListCreditCardsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ListCreditCardsUseCase.class);

    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;
    private final CreditCardMapper creditCardMapper;

    public ListCreditCardsUseCase(
            CreditCardRepository creditCardRepository,
            UserRepository userRepository,
            CreditCardMapper creditCardMapper
    ) {
        this.creditCardRepository = creditCardRepository;
        this.userRepository = userRepository;
        this.creditCardMapper = creditCardMapper;
    }

    public List<CreditCardResponseDTO> execute(String userId) {
        logger.info("Listando cartões para usuário: {}", userId);

        if (!userRepository.existsById(userId)) {
            logger.error("Usuário não encontrado: {}", userId);
            throw new UserNotFoundException("Usuário não encontrado");
        }

        List<CreditCard> cards = creditCardRepository.findByUserId(userId);
        logger.info("Encontrados {} cartões para usuário: {}", cards.size(), userId);

        return cards.stream()
                .map(creditCardMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
