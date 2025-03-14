package ao.com.wundu.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "authentications")
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column
    private String token;

    @Column(nullable = false)
    private String authenticationMethod;

    public Authentication() {}

    public Authentication(String userId, String authenticationMethod) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("ID do usuário é obrigatório");
        if (authenticationMethod == null || authenticationMethod.isBlank()) throw new IllegalArgumentException("Método de autenticação é obrigatório");

        this.userId = userId;
        this.authenticationMethod = authenticationMethod;
    }

    public boolean validateLogin(String providedMethod) {
        return authenticationMethod.equals(providedMethod);
    }

    public String generateToken() {
        this.token = UUID.randomUUID().toString();
        return this.token;
    }

    public void endSession() {
        this.token = null;
    }

    public String getUserId() { return userId; }
    public String getToken() { return token; }
    public String getAuthenticationMethod() { return authenticationMethod; }
} 