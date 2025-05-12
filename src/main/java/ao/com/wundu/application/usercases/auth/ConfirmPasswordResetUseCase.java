package ao.com.wundu.application.usercases.auth;

import ao.com.wundu.application.dtos.ResetPassword.PasswordResetConfirmDTO;
import ao.com.wundu.domain.entities.PasswordResetToken;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.exceptions.InvalidTokenException;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.PasswordResetTokenRepository;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ConfirmPasswordResetUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmPasswordResetUseCase.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void execute(PasswordResetConfirmDTO dto) {
        logger.info("Iniciando confirmação de redefinição de senha com token: {}", dto.token());
        PasswordResetToken resetToken = tokenRepository.findByToken(dto.token())
                .orElseThrow(() -> new InvalidTokenException("Token inválido"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new InvalidTokenException("Token expirado");
        }

        User user = resetToken.getUser();
        if (!userRepository.existsByEmail(user.getEmail())) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        user = resetToken.getUser();
        try {
            user.setPassword(passwordEncoder.encode(dto.newPassword()));
            user.setLoginAttempts(0);
            user.setLocked(false);
            userRepository.save(user);
            logger.info("Senha redefinida para usuário: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Erro ao salvar usuário com nova senha: {}", user.getEmail(), e);
            throw new RuntimeException("Erro ao redefinir senha", e);
        }

        try {
            tokenRepository.delete(resetToken);
            logger.info("Token removido: {}", dto.token());
        } catch (Exception e) {
            logger.error("Erro ao remover token: {}", dto.token(), e);
            throw new RuntimeException("Erro ao remover token", e);
        }
    }
}
