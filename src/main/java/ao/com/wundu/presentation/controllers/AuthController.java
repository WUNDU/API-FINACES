package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.ResetPassword.PasswordResetConfirmDTO;
import ao.com.wundu.application.dtos.ResetPassword.PasswordResetRequestDTO;
import ao.com.wundu.application.dtos.auth.LoginRequestDTO;
import ao.com.wundu.application.dtos.auth.LoginResponseDTO;
import ao.com.wundu.application.usercases.auth.AuthenticateUseCase;
import ao.com.wundu.application.usercases.auth.ConfirmPasswordResetUseCase;
import ao.com.wundu.application.usercases.auth.RequestPasswordResetUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private AuthenticateUseCase authenticate;
    @Autowired
    private RequestPasswordResetUseCase requestPassWordReset;
    @Autowired
    private ConfirmPasswordResetUseCase confirmPassWordReset;

    @PostMapping("login")
    @Operation(summary = "Authenticate user", description = "Logs in a user and returns JWT tokens")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authenticate.authenticate(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("password/reset-request")
    @Operation(summary = "Request password reset", description = "Sends a reset token via email or SMS")
    @ApiResponse(responseCode = "200", description = "Reset request sent")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO dto) throws ServiceUnavailableException {
        requestPassWordReset.execute(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("password/reset-confirm")
    @Operation(summary = "Confirm password reset", description = "Resets the password using a token")
    @ApiResponse(responseCode = "200", description = "Password reset successful")
    @ApiResponse(responseCode = "400", description = "Invalid token")
    public ResponseEntity<Void> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmDTO dto) {
        confirmPassWordReset.execute(dto);
        return ResponseEntity.ok().build();
    }
}
