package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BusinessException{

    public CategoryNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
