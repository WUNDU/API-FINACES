package ao.com.wundu.exception;

import org.springframework.http.HttpStatus;

public class LockedUserException extends BusinessException {

    public LockedUserException(String message) {
        super(message, HttpStatus.LOCKED);
    }
}
