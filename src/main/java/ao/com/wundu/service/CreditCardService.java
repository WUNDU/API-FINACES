package ao.com.wundu.service;

import ao.com.wundu.application.dtos.CreditCardCreateDTO;
import ao.com.wundu.application.dtos.CreditCardResponseDTO;

import java.util.List;

public interface CreditCardService {

    CreditCardResponseDTO addCreditCard(String userId, CreditCardCreateDTO create);
    List<CreditCardResponseDTO> getCreditCardsByUser(String userId);
    CreditCardResponseDTO getCreaditCard(String id);
}
