package ao.com.wundu.application.dtos.plaid;

public class PlaidTokenExchangeResponse {
    private String accessToken;
    private String itemId;

    public PlaidTokenExchangeResponse(String accessToken, String itemId) {
        this.accessToken = accessToken;
        this.itemId = itemId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getItemId() {
        return itemId;
    }
}
