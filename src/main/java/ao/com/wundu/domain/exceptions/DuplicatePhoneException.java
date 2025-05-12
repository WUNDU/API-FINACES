package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicatePhoneException extends BusinessException{

    public DuplicatePhoneException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
