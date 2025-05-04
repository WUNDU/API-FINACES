package ao.com.wundu.application.usercases.user;

import ao.com.wundu.application.dtos.user.UserResponseDTO;
import ao.com.wundu.application.mappers.UserMapper;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class FindAllUsersUseCase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public Page<UserResponseDTO> execute(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponseDTO);
    }
}
