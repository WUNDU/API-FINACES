package ao.com.wundu.dto;

public record LoginResponseDTO(
        String token,
        String refreshToken,
        LoginResponseUserDTO userDTO
) {
}
