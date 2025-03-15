package ao.com.wundu.service;

import ao.com.wundu.dto.LoginRequestDTO;
import ao.com.wundu.dto.LoginResponseDTO;
import ao.com.wundu.dto.PasswordResetConfirmDTO;
import ao.com.wundu.dto.PasswordResetRequestDTO;

public interface AuthService {

    LoginResponseDTO authenticate(LoginRequestDTO dto);
    void requestPassWordReset(PasswordResetRequestDTO dto);
    void confirmPassWordReset(PasswordResetConfirmDTO dto);

}
