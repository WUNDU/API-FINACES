package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends BusinessException{

    public UnauthorizedAccessException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
