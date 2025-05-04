package ao.com.wundu.application.usercases.auth;

import ao.com.wundu.application.dtos.auth.LoginRequestDTO;
import ao.com.wundu.application.dtos.auth.LoginResponseDTO;
import ao.com.wundu.application.dtos.auth.LoginResponseUserDTO;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.exceptions.InvalidCredentialsException;
import ao.com.wundu.domain.exceptions.LockedUserException;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import ao.com.wundu.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticateUseCase {

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int LOCK_DURATION_MINUTES = 15;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new InvalidCredentialsException("E-mail ou senha incorreta"));

        if (user.isLocked() && user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new LockedUserException("Usuário bloqueado até " + user.getLockedUntil());
        }

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            if (user.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
                user.setLocked(true);
                user.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
                user.setLoginAttempts(0);
                userRepository.save(user);
                throw new LockedUserException("Usuário bloqueado por " + LOCK_DURATION_MINUTES + " minutos");
            }
            userRepository.save(user);
            throw new InvalidCredentialsException("E-mail ou senha incorreta");
        }

        user.setLoginAttempts(0);
        user.setLocked(false);
        user.setLockedUntil(null);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        LoginResponseUserDTO userDTO = new LoginResponseUserDTO(user.getId(), user.getName(), user.getEmail());

        return new LoginResponseDTO(token, refreshToken, userDTO);
    }
}


//    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
//
//        User user = userRepository.findByEmail(dto.email())
//                .orElseThrow( () -> new InvalidCredentialsException("E-mail ou senha incorreta") );
//
//        if (user.isLocked() && user.getLockedUntil().isAfter(LocalDateTime.now())) {
//            throw new LockedUserException("Usuário bloqueado temporariamente. Tente novamente após " +
//                    user.getLockedUntil());
//        }
//
//        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
//            user.setLoginAttempts(user.getLoginAttempts() + 1);
//            if (user.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
//                user.setLocked(true);
//                user.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
//                user.setLoginAttempts(0);
//                userRepository.save(user);
//                throw new LockedUserException("Usuário bloqueado por " + LOCK_DURATION_MINUTES +
//                        " minutos");
//            }
//            userRepository.save(user);
//            throw new InvalidCredentialsException("E-mail ou senha incorreta");
//        }
//        user.setLoginAttempts(0);
//        user.setLocked(false);
//        user.setLockedUntil(null);
//        userRepository.save(user);
//
//        String token = jwtUtil.gereratToken(user.getEmail());
//        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
//
//        LoginResponseUserDTO userDTO = new LoginResponseUserDTO(user.getId(), user.getName(), user.getEmail());
//
//        return new LoginResponseDTO(token, refreshToken, userDTO);
//    }
//
//    @Override
//    public void requestPassWordReset(PasswordResetRequestDTO dto) {
//
//        User user = userRepository.findByEmail(dto.email())
//                .orElseThrow( () -> new UserNotFoundException("Usuário não encontrado") );
//
//        String token = UUID.randomUUID().toString();
//        resetTokens.put(token, dto.email()); // Armazena o token associado ao e-mail
//        // Simula armazenamento temporário do token (em produção, usar Redis ou tabela)
//        String message = "Seu código de recuperação é: " + token;
//
//        if ( "email".equals(dto.method()) ) {
//            emailService.sendEmail(dto.email(), "Recuepração de senha",  message);
//        } else if ( "sms".equals(dto.method()) ) {
//            smsService.sendSms("+244" + user.getPhone(), message);
//        } else {
//            throw new InvalidCredentialsException("Método de recuperação inválido");
//        }
//    }
//
//    @Override
//    public void confirmPassWordReset(PasswordResetConfirmDTO dto) {
//        // Simula validação do token (em produção, verificar em Redis ou tabela)
//
//        String email = resetTokens.get(dto.token());
//        if (email == null) {
//            throw new InvalidTokenException("Token inválido");
//        }
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
//        user.setPassword(passwordEncoder.encode(dto.newPassword()));
//        user.setLoginAttempts(0);
//        user.setLocked(false);
//        userRepository.save(user);
//        resetTokens.remove(dto.token()); // Remove o token após uso
//    }
