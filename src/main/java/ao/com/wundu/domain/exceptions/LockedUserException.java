package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class LockedUserException extends BusinessException {

    public LockedUserException(String message) {
        super(message, HttpStatus.LOCKED);
    }
}
