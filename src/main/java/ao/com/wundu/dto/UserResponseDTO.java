package ao.com.wundu.dto;

import ao.com.wundu.entity.Notification;
import ao.com.wundu.enums.PlanType;

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
