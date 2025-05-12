package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRecoveryMethodException extends BusinessException {

    public InvalidRecoveryMethodException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
