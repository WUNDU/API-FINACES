package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends BusinessException{

    public TransactionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
