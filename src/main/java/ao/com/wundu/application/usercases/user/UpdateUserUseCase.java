package ao.com.wundu.application.usercases.user;

import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.dtos.user.UserUpdateDTO;
import ao.com.wundu.application.mappers.UserMapper;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.exceptions.DuplicateEmailException;
import ao.com.wundu.domain.exceptions.DuplicatePhoneException;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserUseCase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserResponseDTO execute(String id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (dto.phone() != null && !dto.phone().equals(user.getPhone()) && userRepository.existsByPhone(dto.phone())) {
            throw new DuplicatePhoneException("Já existe um usuário com este telefone");
        }

        user.setName(dto.name());
        user.setPhone(dto.phone());
        user.setNotificationPreference(
                dto.notificationPreference() != null ? dto.toNotificationPreference() : user.getNotificationPreference());

        user = userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }
}
