package ao.com.wundu.service.impl;

import ao.com.wundu.dto.CreditCardCreateDTO;
import ao.com.wundu.dto.CreditCardResponseDTO;
import ao.com.wundu.entity.CreditCard;
import ao.com.wundu.entity.User;
import ao.com.wundu.repository.CreditCardRepository;
import ao.com.wundu.repository.UserRepository;
import ao.com.wundu.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CreditCardResponseDTO addCreditCard(String userId, CreditCardCreateDTO create) {

        User user = userRepository.findById(userId)
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado") );

        if ( creditCardRepository.findByUserId(userId).size() >= 3 ) {
            throw new IllegalArgumentException("Limite de 5 cartões por usuário atingido");
        }

        CreditCard card = new CreditCard();
        card.setCardNumber(create.cardNumber());
        card.setBankName(create.bankName());
        card.setCreditLimit(create.creaditLimit());
        card.setExpirationDate(create.expirationDate());
        card.setUser(user);

        card = creditCardRepository.save(card);

        return new CreditCardResponseDTO(
                card.getId(), card.getCardNumber(), card.getBankName(), card.getCreditLimit(),
                card.getExpirationDate(), userId);

    }

    @Override
    public List<CreditCardResponseDTO> getCreditCardsByUser(String userId) {

        if ( !userRepository.existsById(userId) ) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        List<CreditCard> cards = creditCardRepository.findByUserId(userId);

        return cards.stream()
                .map( card -> new CreditCardResponseDTO(card.getId(), card.getCardNumber(), card.getBankName(),
                        card.getCreditLimit(), card.getExpirationDate(), userId) )
                .collect(Collectors.toList());

    }

    @Override
    public CreditCardResponseDTO getCreaditCard(String id) {

        CreditCard card = creditCardRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Cartão não encontrado") );

        return new CreditCardResponseDTO(card.getId(), card.getCardNumber(), card.getBankName(),
                card.getCreditLimit(), card.getExpirationDate(), card.getUser().getId());
    }
}
