package ao.com.wundu.application.dtos.plaid;

public class PlaidTokenExchangeRequest {
    private String publicToken;

    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }
}
