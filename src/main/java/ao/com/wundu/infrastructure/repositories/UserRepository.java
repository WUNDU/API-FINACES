package ao.com.wundu.infrastructure.repositories;

import ao.com.wundu.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Page<User> findAll(Pageable pageable);
}
