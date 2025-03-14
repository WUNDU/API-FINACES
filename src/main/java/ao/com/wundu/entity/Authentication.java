package ao.com.wundu.entity;

import java.util.UUID;

public class Authentication {
    private final String userId;
    private String token;
    private final String authenticationMethod;

    public Authentication(String userId, String authenticationMethod) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("ID do usuário é obrigatório");
        if (authenticationMethod == null || authenticationMethod.isBlank()) throw new IllegalArgumentException("Método de autenticação é obrigatório");

        this.userId = userId;
        this.authenticationMethod = authenticationMethod;
    }

    public boolean validarLogin(String providedToken) {
        return token != null && token.equals(providedToken);
    }

    public void gerarToken() {
        this.token = UUID.randomUUID().toString();
    }

    public void encerrarSessao() {
        this.token = null;
    }

    public String getUserId() { return userId; }
    public String getToken() { return token; }
    public String getAuthenticationMethod() { return authenticationMethod; }
} 
