package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCardException extends BusinessException {

    public InvalidCardException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
