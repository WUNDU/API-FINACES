package ao.com.wundu.application.dtos.user;

public record UserResponseDTO(
        String id,
        String name,
        String email,
        String phone,
        String notificationPreference,
        String planType,
        int creditCardCount
) {
}
