package ao.com.wundu.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BusinessException{

    public CategoryNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
