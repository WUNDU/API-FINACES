package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends BusinessException{

    public NotificationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
