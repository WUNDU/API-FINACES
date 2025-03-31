package ao.com.wundu.service.impl;

import ao.com.wundu.dto.*;
import ao.com.wundu.entity.CreditCard;
import ao.com.wundu.entity.User;
import ao.com.wundu.exception.DuplicateEmailException;
import ao.com.wundu.exception.DuplicatePhoneException;
import ao.com.wundu.exception.UserNotFoundException;
import ao.com.wundu.messaging.EmailService;
import ao.com.wundu.repository.CreditCardRepository;
import ao.com.wundu.repository.UserRepository;
import ao.com.wundu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public UserResponseDTO createUser(UserCreateDTO create) {

        if ( userRepository.findByEmail(create.email()).isPresent() ) {
            throw new DuplicateEmailException("Já existe um usuário com este email");
        }

        if ( userRepository.findByPhone(create.phone()).isPresent()) {
            throw new DuplicatePhoneException("Já existe um usuário com este Telefone");
        }


        User user = new User(create.name(), create.email(), passwordEncoder.encode(create.password()), create.phone(), create.toNotificationPreference());
        user = userRepository.save(user);
        sendWelcomeEmail(user);
        return mapToResponseDTO(user);
//        // Enviar e-mail de boas-vindas
//        String subject = "Bem-vindo ao nosso sistema!";
//        String message = "Olá " + user.getName() + ",\n\n"
//                + "Seu cadastro foi realizado com sucesso. Estamos felizes em tê-lo(a) conosco!\n\n"
//                + "Se precisar de algo, entre em contato.\n\n"
//                + "Atenciosamente,\nWUNDU FINACES";
//
//        emailService.sendEmail(user.getEmail(), subject, message);
//
//        // Enviar notificação para o admin
//        String adminEmail = "evandre297@email.com";
//        String subjectAdmin = "Novo usuário cadastrado!";
//        String messageAdmin = "Olá Admin,\n\n"
//                + "Um novo usuário acaba de se cadastrar no sistema.\n\n"
//                + "**Nome:** " + user.getName() + "\n"
//                + "**Email:** " + user.getEmail() + "\n\n"
//                + "Atenciosamente,\nEquipe do Sistema";

    }

    @Override
    public UserResponseDTO updateUser(String id, UserUpdateDTO update) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException("Usuário não encontrado") );

        user.setName(update.name());
        user.setNotificationPreference(update.toNotificationPreference());

        user = userRepository.save(user);
        return mapToResponseDTO(user);
    }

    @Override
    public UserResponseDTO findUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException("Usuário não encontrado") );

        return mapToResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {

//        return userRepository.findAll().stream()
//                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(),
//                        user.getNotificationPreference().getValue(), user.getPlanType().getValue()))
//                .collect(Collectors.toList());

        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public void deleteUser(String id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }

    private void sendWelcomeEmail(User user) {
        String subject = "Bem-vindo ao Wundu Finances!";
        String message = "Olá " + user.getEmail() + " .\n\n" +
                "Voce acaba de dara o primeiro passo para transformar sua vida financeira com o Wundu Finances! " +
                "Estamos emplogados para ajuda-lo a alcancar seus objectivos. Explore agora e descubra como podemos fazer a difrenca juntos!\n\n" +
                "Atenciosamente \nEquipe Wundu Finances";

        emailService.sendEmail(user.getEmail(), subject, message);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getNotificationPreference().getValue(),
                user.getPlanType().getValue(),
                user.getCreditCards().size()
        );
    }
}
