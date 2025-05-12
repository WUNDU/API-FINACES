package ao.com.wundu.application.usercases.user;

import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.mappers.UserMapper;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.exceptions.DuplicateEmailException;
import ao.com.wundu.domain.exceptions.DuplicatePhoneException;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.services.EmailService;
import ao.com.wundu.infrastructure.services.impl.EmailServiceImpl;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;

@Service
public class RegisterUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserUseCase.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private UserMapper userMapper;

    public UserResponseDTO execute(UserCreateDTO dto) throws ServiceUnavailableException {
        if (userRepository.existsByEmail(dto.email())) {
            throw new DuplicateEmailException("Já existe um usuário com este email");
        }
        if (userRepository.existsByPhone(dto.phone())) {
            throw new DuplicatePhoneException("Já existe um usuário com este telefone");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user = userRepository.save(user);

        sendWelcomeEmail(user);

        return userMapper.toResponseDTO(user);
    }

    @Retry(name = "emailRetry")
    private void sendWelcomeEmail(User user) throws ServiceUnavailableException {
        try {
            String subject = "Bem-vindo ao Wundu Finances!";
            String message = String.format("""
                    Olá %s,

                    Você acaba de dar o primeiro passo para transformar sua vida financeira com o Wundu Finances!
                    Estamos empolgados para ajudá-lo a alcançar seus objetivos. Explore agora e descubra como podemos fazer a diferença juntos!

                    Atenciosamente,
                    Equipe Wundu Finances
                    """, user.getName());
            emailService.sendEmail(user.getEmail(), subject, message);
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail de boas-vindas para {}", user.getEmail(), e);
            throw new ServiceUnavailableException("Falha ao enviar e-mail de boas-vindas");
        }
    }

//    @Override
//    public UserResponseDTO createUser(UserCreateDTO create) {
//
//        if ( userRepository.findByEmail(create.email()).isPresent() ) {
//            throw new DuplicateEmailException("Já existe um usuário com este email");
//        }
//
//        if ( userRepository.findByPhone(create.phone()).isPresent()) {
//            throw new DuplicatePhoneException("Já existe um usuário com este Telefone");
//        }
//
//        User user = new User(create.name(), create.email(), passwordEncoder.encode(create.password()), create.phone(), create.toNotificationPreference());
//        user = userRepository.save(user);
////        sendWelcomeEmail(user);
//
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
//        return mapToResponseDTO(user);
//
//    }
//
//    @Override
//    public UserResponseDTO updateUser(String id, UserUpdateDTO update) {
//
//        User user = userRepository.findById(id)
//                .orElseThrow( () -> new UserNotFoundException("Usuário não encontrado") );
//
//        user.setName(update.name());
//        user.setNotificationPreference(update.toNotificationPreference());
//
//        user = userRepository.save(user);
//        return mapToResponseDTO(user);
//    }
//
//    @Override
//    public UserResponseDTO findUserById(String id) {
//
//        User user = userRepository.findById(id)
//                .orElseThrow( () -> new UserNotFoundException("Usuário não encontrado") );
//
//        return mapToResponseDTO(user);
//    }
//
//    @Override
//    public List<UserResponseDTO> findAllUsers() {
//
////        return userRepository.findAll().stream()
////                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(),
////                        user.getNotificationPreference().getValue(), user.getPlanType().getValue()))
////                .collect(Collectors.toList());
//
//        return userRepository.findAll().stream()
//                .map(this::mapToResponseDTO)
//                .toList();
//    }
//
//    @Override
//    public void deleteUser(String id) {
//
//        if (!userRepository.existsById(id)) {
//            throw new UserNotFoundException("Usuário não encontrado");
//        }
//
//        userRepository.deleteById(id);
//    }
//
//    private void sendWelcomeEmail(User user) {
//        String subject = "Bem-vindo ao Wundu Finances!";
//        String message = "Olá " + user.getEmail() + " .\n\n" +
//                "Voce acaba de dara o primeiro passo para transformar sua vida financeira com o Wundu Finances! " +
//                "Estamos emplogados para ajuda-lo a alcancar seus objectivos. Explore agora e descubra como podemos fazer a difrenca juntos!\n\n" +
//                "Atenciosamente \nEquipe Wundu Finances";
//
//        emailService.sendEmail(user.getEmail(), subject, message);
//    }
//
//    private UserResponseDTO mapToResponseDTO(User user) {
//        return new UserResponseDTO(
//                user.getId(),
//                user.getName(),
//                user.getEmail(),
//                user.getPhone(),
//                user.getNotificationPreference().getValue(),
//                user.getPlanType().getValue(),
//                user.getCreditCards().size()
//        );
//    }
}
