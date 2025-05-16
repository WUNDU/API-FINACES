package ao.com.wundu.domain.exceptions;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;

public class SmsSendingException extends BusinessException {

    public SmsSendingException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
