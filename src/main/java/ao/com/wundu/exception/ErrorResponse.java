package ao.com.wundu.exception;


import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldErrorResponse> fieldErrors;

    public ErrorResponse() {
    }

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path, List<FieldErrorResponse> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FieldErrorResponse> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorResponse> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
