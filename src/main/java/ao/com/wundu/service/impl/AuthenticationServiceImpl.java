
package ao.com.wundu.service.impl;

import ao.com.wundu.dto.AuthenticationRequestDTO;
import ao.com.wundu.dto.AuthenticationResponseDTO;
import ao.com.wundu.entity.Authentication;
import ao.com.wundu.repository.AuthenticationRepository;
import ao.com.wundu.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationRepository repository;

    public AuthenticationServiceImpl(AuthenticationRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuthenticationResponseDTO validateLogin(AuthenticationRequestDTO request) {
        Authentication auth = repository.findByUserId(request.userId())
                .orElse(new Authentication(request.userId(), request.authenticationMethod()));

        if (!auth.validateLogin(request.authenticationMethod())) {
            throw new IllegalArgumentException("Método de autenticação inválido");
        }

        String token = auth.generateToken();
        repository.save(auth);
        return new AuthenticationResponseDTO(token);
    }

    @Override
    public void endSession(String userId) {
        Authentication auth = repository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        auth.endSession();
        repository.save(auth);
    }
}
