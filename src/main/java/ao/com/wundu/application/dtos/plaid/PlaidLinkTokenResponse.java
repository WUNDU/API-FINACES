package ao.com.wundu.application.dtos.plaid;

public class PlaidLinkTokenResponse {
    private String linkToken;

    public PlaidLinkTokenResponse(String linkToken) {
        this.linkToken = linkToken;
    }

    public String getLinkToken() {
        return linkToken;
    }
}
