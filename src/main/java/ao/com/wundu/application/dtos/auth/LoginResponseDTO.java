package ao.com.wundu.application.dtos.auth;

public record LoginResponseDTO(
        String token,
        String refreshToken,
        LoginResponseUserDTO user
) {
}
