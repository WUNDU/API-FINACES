package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class PushNotificationException extends BusinessException {

    public PushNotificationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
