package ao.com.wundu.service;

import ao.com.wundu.dto.UserCreateDTO;
import ao.com.wundu.dto.UserListDTO;
import ao.com.wundu.dto.UserUpdateDTO;

import java.util.List;

public interface UserService {

    UserListDTO createUser(UserCreateDTO create);
    UserListDTO updateUser(String id, UserUpdateDTO update);
    UserListDTO findUserById(String id);
    List<UserListDTO> findAllUsers();
    void deleteUser(String id);
}
