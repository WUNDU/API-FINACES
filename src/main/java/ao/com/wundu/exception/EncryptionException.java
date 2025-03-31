package ao.com.wundu.exception;

import org.springframework.http.HttpStatus;

public class EncryptionException extends CryptoException{

    public EncryptionException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }
}
