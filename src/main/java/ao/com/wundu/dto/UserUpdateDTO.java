package ao.com.wundu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 a 100 caracteres")
        String name,

//        @Size(min = 8, message = "Password deve ter no mínimo 8 caracteres")
//        String password,

        String notificationPreference
) {
}
