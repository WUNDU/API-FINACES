package ao.com.wundu.controller;

import ao.com.wundu.dto.AuthenticationRequestDTO;
import ao.com.wundu.dto.AuthenticationResponseDTO;
import ao.com.wundu.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO request) {
        AuthenticationResponseDTO response = authenticationService.validateLogin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String userId) {
        authenticationService.endSession(userId);
        return ResponseEntity.noContent().build();
    }
}