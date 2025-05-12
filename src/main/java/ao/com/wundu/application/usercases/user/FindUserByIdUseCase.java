package ao.com.wundu.application.usercases.user;

import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.mappers.UserMapper;
import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindUserByIdUseCase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserResponseDTO execute(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return userMapper.toResponseDTO(user);
    }

}
