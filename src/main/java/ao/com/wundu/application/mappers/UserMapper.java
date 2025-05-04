package ao.com.wundu.application.mappers;

import ao.com.wundu.application.dtos.user.UserCreateDTO;
import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.dtos.user.UserUpdateDTO;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.enums.NotificationPreference;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper() {
        this.modelMapper = new ModelMapper();

        // Configuração do mapeamento UserCreateDTO -> User
        Converter<UserCreateDTO, User> userCreateDTOConverter = ctx -> {
            UserCreateDTO src = ctx.getSource();
            User user = new User(
                    src.name(),
                    src.email(),
                    src.password(),
                    src.phone(),
                    src.notificationPreference() != null ? NotificationPreference.fromValue(src.notificationPreference()) : NotificationPreference.EMAIL
            );
            return user;
        };
        modelMapper.createTypeMap(UserCreateDTO.class, User.class)
                .setConverter(userCreateDTOConverter);

        // Configuração do mapeamento User -> UserResponseDTO
        Converter<User, UserResponseDTO> userToResponseDTOConverter = ctx -> {
            User src = ctx.getSource();
            return new UserResponseDTO(
                    src.getId(),
                    src.getName(),
                    src.getEmail(),
                    src.getPhone(),
                    src.getNotificationPreference() != null ? src.getNotificationPreference().getValue() : "email",
                    src.getPlanType() != null ? src.getPlanType().getValue() : "free",
                    src.getCreditCards() != null ? src.getCreditCards().size() : 0
            );
        };
        modelMapper.createTypeMap(User.class, UserResponseDTO.class)
                .setConverter(userToResponseDTOConverter);

        // Configuração do mapeamento UserUpdateDTO -> User
        Converter<UserUpdateDTO, User> userUpdateDTOConverter = ctx -> {
            UserUpdateDTO src = ctx.getSource();
            User user = new User();
            user.setName(src.name());
            user.setPhone(src.phone());
            user.setNotificationPreference(
                    src.notificationPreference() != null ? NotificationPreference.fromValue(src.notificationPreference()) : NotificationPreference.EMAIL
            );
            return user;
        };
        modelMapper.createTypeMap(UserUpdateDTO.class, User.class)
                .setConverter(userUpdateDTOConverter);
    }

    public User toEntity(UserCreateDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserResponseDTO toResponseDTO(User user) {
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public User toEntity(UserUpdateDTO dto) {
        return modelMapper.map(dto, User.class);
    }
}
