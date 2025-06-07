package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistsException extends BusinessException{
    public CategoryAlreadyExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
