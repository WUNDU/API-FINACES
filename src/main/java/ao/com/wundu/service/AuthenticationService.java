package ao.com.wundu.service;

import ao.com.wundu.dto.AuthenticationRequestDTO;
import ao.com.wundu.dto.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO login(AuthenticationRequestDTO request);
    void logout(String userId);
}
