package ao.com.wundu.service;

import ao.com.wundu.dto.AuthenticationRequestDTO;
import ao.com.wundu.dto.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO validateLogin(AuthenticationRequestDTO requestDTO);
    void endSession(String userId);
}
