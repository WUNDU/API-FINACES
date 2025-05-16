package ao.com.wundu.domain.exceptions;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;

public class EmailSendingException extends BusinessException{

    public EmailSendingException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
