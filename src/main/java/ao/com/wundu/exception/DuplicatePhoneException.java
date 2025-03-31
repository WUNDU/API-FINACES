package ao.com.wundu.exception;

import org.springframework.http.HttpStatus;

public class DuplicatePhoneException extends BusinessException{

    public DuplicatePhoneException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
