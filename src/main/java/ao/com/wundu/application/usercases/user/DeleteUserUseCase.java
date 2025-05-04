package ao.com.wundu.application.usercases.user;

import ao.com.wundu.domain.entities.User;
import ao.com.wundu.domain.exceptions.UserNotFoundException;
import ao.com.wundu.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserUseCase {

    @Autowired
    private UserRepository userRepository;

    public void execute(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        user.setDeleted(true);
        userRepository.save(user);
    }
}
