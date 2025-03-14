package ao.com.wundu.repository;

import ao.com.wundu.entity.Authentication;
import java.util.Optional;

public interface AuthenticationRepository {
    Optional<Authentication> findByUserId(String userId);
    Authentication save(Authentication authentication);
}