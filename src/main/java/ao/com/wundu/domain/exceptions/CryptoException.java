package ao.com.wundu.domain.exceptions;

import org.springframework.http.HttpStatus;

public class CryptoException extends RuntimeException{

    private final HttpStatus status;

    public CryptoException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CryptoException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
