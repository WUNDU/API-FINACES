package ao.com.wundu.service.impl;

import ao.com.wundu.config.JwtUtil;
import ao.com.wundu.dto.*;
import ao.com.wundu.entity.User;
import ao.com.wundu.messaging.EmailService;
import ao.com.wundu.messaging.SmsService;
import ao.com.wundu.repository.UserRepository;
import ao.com.wundu.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int LOCK_DURATION_MINUTES = 15;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SmsService smsService;

    // Mapa temporário para armazenar tokens de recuperação (em produção, usar Redis)
    private final Map<String, String> resetTokens = new HashMap<>();

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow( () -> new RuntimeException("E-mail ou senha incorreta") );

        if (user.isLocked()) {
            throw new RuntimeException("Usuário bloqueado temporariamente. Tente novamente após " +
                    user.getLockedUntil().toString());
        }

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            if (user.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
                user.setLoginAttempts(0);
                userRepository.save(user);
                throw new RuntimeException("Usuário bloqueado por " + LOCK_DURATION_MINUTES +
                        " minutos devido a múltiplas tentativas falhas");
            }
            userRepository.save(user);
            throw new RuntimeException("E-mail ou senha incorreta");
        }
        user.setLoginAttempts(0);
        user.setLockedUntil(null);
        userRepository.save(user);

        String token = jwtUtil.gereratToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        LoginResponseUserDTO userDTO = new LoginResponseUserDTO(user.getId(), user.getName(), user.getEmail());

        return new LoginResponseDTO(token, refreshToken, userDTO);
    }

    @Override
    public void requestPassWordReset(PasswordResetRequestDTO dto) {

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow( () -> new RuntimeException("Usuário não encontrado") );

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, dto.email()); // Armazena o token associado ao e-mail
        // Simula armazenamento temporário do token (em produção, usar Redis ou tabela)
        String message = "Seu código de recuperação é: " + token;

        if ( "email".equals(dto.method()) ) {
            emailService.sendEmail(dto.email(), "Recuepração de senha",  message);
        } else if ( "sms".equals(dto.method()) ) {
            smsService.sendSms("+244" + user.getPhone(), message);
        } else {
            throw new RuntimeException("Método de recuperação inválido");
        }
    }

    @Override
    public void confirmPassWordReset(PasswordResetConfirmDTO dto) {
        // Simula validação do token (em produção, verificar em Redis ou tabela)

        String email = resetTokens.get(dto.token());
        if (email == null) {
            throw new RuntimeException("Token inválido");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        user.setLoginAttempts(0);
        user.setLocked(false);
        userRepository.save(user);
        resetTokens.remove(dto.token()); // Remove o token após uso
    }
}
