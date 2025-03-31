package ao.com.wundu.exception;

import org.springframework.http.HttpStatus;

public class InvalidTransactionAmountException extends BusinessException{

    public InvalidTransactionAmountException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
