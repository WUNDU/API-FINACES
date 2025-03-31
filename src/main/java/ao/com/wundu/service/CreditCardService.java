package ao.com.wundu.service;

import ao.com.wundu.dto.CreditCardCreateDTO;
import ao.com.wundu.dto.CreditCardResponseDTO;

import java.util.List;

public interface CreditCardService {

    CreditCardResponseDTO addCreditCard(String userId, CreditCardCreateDTO create);
    List<CreditCardResponseDTO> getCreditCardsByUser(String userId);
    CreditCardResponseDTO getCreaditCard(String id);
}
