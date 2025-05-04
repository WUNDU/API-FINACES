package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class CreditCardNotFoundException extends BusinessException{

    public CreditCardNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
