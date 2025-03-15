package ao.com.wundu.service.impl;

import ao.com.wundu.dto.*;
import ao.com.wundu.entity.CreditCard;
import ao.com.wundu.entity.User;
import ao.com.wundu.repository.CreditCardRepository;
import ao.com.wundu.repository.UserRepository;
import ao.com.wundu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserCreateDTO create) {

        if ( userRepository.findByEmail(create.email()).isPresent() ) {
            throw new IllegalArgumentException("Já existe um usuário com este email");
        }

        User user = new User(create.name(), create.email(), passwordEncoder.encode(create.password()), create.notificationPreference());
        user = userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getNotificationPreference());

    }

    @Override
    public UserResponseDTO updateUser(String id, UserUpdateDTO update) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado") );

        user.setName(update.name());
//        user.setName(update.password());
        user.setNotificationPreference(update.notificationPreference());

        user = userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getNotificationPreference());
    }

    @Override
    public UserResponseDTO findUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado") );

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getNotificationPreference());
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {

        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getNotificationPreference()))
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
