package ao.com.wundu.application.usercases.notification;

import ao.com.wundu.application.dtos.notification.NotificationPreferenceDTO;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateNotificationPreferenceUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateNotificationPreferenceUseCase.class);

    private final UserRepository userRepository;

    public UpdateNotificationPreferenceUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String userId, NotificationPreferenceDTO dto) {
        logger.info("Atualizando preferência de notificação para usuário: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado: {}", userId);
                    return new UserNotFoundException("Usuário não encontrado");
                });

        NotificationPreference preference = NotificationPreference.fromValue(dto.preference());
        user.setNotificationPreference(preference);
        userRepository.save(user);

        logger.info("Preferência de notificação atualizada para: {}", preference);
    }
}
