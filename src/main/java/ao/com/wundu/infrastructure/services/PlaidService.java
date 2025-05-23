package ao.com.wundu.infrastructure.services;

import ao.com.wundu.application.dtos.plaid.PlaidLinkTokenResponse;
import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeRequest;
import ao.com.wundu.application.dtos.plaid.PlaidTokenExchangeResponse;

public interface PlaidService {
    PlaidTokenExchangeResponse exchangePublicToken(PlaidTokenExchangeRequest request) throws Exception;

    PlaidLinkTokenResponse createLinkToken(String userId) throws Exception;
}
