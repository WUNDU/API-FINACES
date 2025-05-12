package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class CreditCardLimitExceededException extends BusinessException{

    public CreditCardLimitExceededException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
