package ao.com.wundu.dto;

public record UserResponseDTO(
        String id,
        String name,
        String email,
        String phone,
        String notificationPreference
) {
}
