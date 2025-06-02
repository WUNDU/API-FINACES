package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class CreditCardAlreadyAssociatedException extends BusinessException{
    public CreditCardAlreadyAssociatedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
