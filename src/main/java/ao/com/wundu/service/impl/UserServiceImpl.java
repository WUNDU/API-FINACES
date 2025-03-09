package ao.com.wundu.service.impl;

import ao.com.wundu.dto.*;
import ao.com.wundu.entity.CreditCard;
import ao.com.wundu.entity.User;
import ao.com.wundu.repository.CreditCardRepository;
import ao.com.wundu.repository.UserRepository;
import ao.com.wundu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Override
    public UserResponseDTO createUser(UserCreateDTO create) {

        if ( userRepository.findByEmail(create.email()).isPresent() ) {
            throw new IllegalArgumentException("Já existe um usuário com este email");
        }

        User user = new User(create.name(), create.email(), create.password());
        user = userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());

    }

    @Override
    public UserResponseDTO updateUser(String id, UserUpdateDTO update) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado") );

        user.setName(update.name());
        user.setName(update.password());

        user = userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public UserResponseDTO findUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado") );

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {

        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }

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
}
