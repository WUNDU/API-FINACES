package ao.com.wundu.service.impl;

import ao.com.wundu.dto.UserCreateDTO;
import ao.com.wundu.dto.UserListDTO;
import ao.com.wundu.dto.UserUpdateDTO;
import ao.com.wundu.entity.User;
import ao.com.wundu.repository.UserRepository;
import ao.com.wundu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserListDTO createUser(UserCreateDTO create) {

        if ( userRepository.findByEmail(create.email()).isPresent() ) {
            throw new IllegalArgumentException("Já existe um usuário com este email");
        }

        User user = new User(create.name(), create.email(), create.password());
        user = userRepository.save(user);

        return new UserListDTO(user.getId(), user.getName(), user.getEmail(), user.getAccounts().size());

    }

    @Override
    public UserListDTO updateUser(String id, UserUpdateDTO update) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado") );

        if (update.name() != null) user.setName(update.name());
        if (update.password() != null) user.setName(update.password());

        user = userRepository.save(user);
        return new UserListDTO(user.getId(), user.getName(), user.getEmail(), user.getAccounts().size());
    }

    @Override
    public UserListDTO findUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado") );

        return new UserListDTO(user.getId(), user.getName(), user.getEmail(), user.getAccounts().size());
    }

    @Override
    public List<UserListDTO> findAllUsers() {

        return userRepository.findAll().stream()
                .map(user -> new UserListDTO(user.getId(), user.getName(), user.getEmail(), user.getAccounts().size()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }
}
