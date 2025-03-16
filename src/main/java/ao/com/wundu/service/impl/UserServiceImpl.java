package ao.com.wundu.service.impl;

import ao.com.wundu.dto.*;
import ao.com.wundu.entity.CreditCard;
import ao.com.wundu.entity.User;
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
            throw new IllegalArgumentException("Já existe um usuário com este email");
        }

        User user = new User(create.name(), create.email(), passwordEncoder.encode(create.password()), create.phone(), create.notificationPreference());
        user = userRepository.save(user);

        // Enviar e-mail de boas-vindas
        String subject = "Bem-vindo ao nosso sistema!";
        String message = "Olá " + user.getName() + ",\n\n"
                + "Seu cadastro foi realizado com sucesso. Estamos felizes em tê-lo(a) conosco!\n\n"
                + "Se precisar de algo, entre em contato.\n\n"
                + "Atenciosamente,\nWUNDU FINACES";

        emailService.sendEmail(user.getEmail(), subject, message);

        // Enviar notificação para o admin
        String adminEmail = "evandre297@email.com";
        String subjectAdmin = "Novo usuário cadastrado!";
        String messageAdmin = "Olá Admin,\n\n"
                + "Um novo usuário acaba de se cadastrar no sistema.\n\n"
                + "**Nome:** " + user.getName() + "\n"
                + "**Email:** " + user.getEmail() + "\n\n"
                + "Atenciosamente,\nEquipe do Sistema";

        emailService.sendEmail(adminEmail, subjectAdmin, messageAdmin);

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getNotificationPreference());

    }

    @Override
    public UserResponseDTO updateUser(String id, UserUpdateDTO update) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado") );

        user.setName(update.name());
//        user.setName(update.password());
        user.setNotificationPreference(update.notificationPreference());

        user = userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getNotificationPreference());
    }

    @Override
    public UserResponseDTO findUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado") );

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getNotificationPreference());
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {

        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getNotificationPreference()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }
}
