package ao.com.wundu.exception;

import org.springframework.http.HttpStatus;

public class InvalidRecoveryMethodException extends BusinessException {

    public InvalidRecoveryMethodException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
