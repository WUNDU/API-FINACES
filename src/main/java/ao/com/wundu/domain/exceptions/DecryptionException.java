package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class DecryptionException extends CryptoException{

    public DecryptionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public DecryptionException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
