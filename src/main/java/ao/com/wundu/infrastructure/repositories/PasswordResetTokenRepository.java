package ao.com.wundu.infrastructure.repositories;

import ao.com.wundu.domain.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findByToken(String token);
    void delete(PasswordResetToken token);
}
