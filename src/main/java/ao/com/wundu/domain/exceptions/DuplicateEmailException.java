package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends BusinessException{

    public DuplicateEmailException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
