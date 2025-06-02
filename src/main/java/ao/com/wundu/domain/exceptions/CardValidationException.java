package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class CardValidationException extends BusinessException{

    public CardValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
