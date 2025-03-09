package ao.com.wundu.service;

import ao.com.wundu.dto.*;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserCreateDTO create);
    UserResponseDTO updateUser(String id, UserUpdateDTO update);
    UserResponseDTO findUserById(String id);
    List<UserResponseDTO> findAllUsers();
    void deleteUser(String id);

    CreditCardResponseDTO addCreditCard(String userId, CreditCardCreateDTO create);
}
