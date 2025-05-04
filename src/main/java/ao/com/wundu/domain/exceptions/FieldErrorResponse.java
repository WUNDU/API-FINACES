package ao.com.wundu.domain.exceptions;

public class FieldErrorResponse {

    private String field;
    private String message;
    private Object rejectedValue;

    public FieldErrorResponse() {
    }

    public FieldErrorResponse(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }
}
