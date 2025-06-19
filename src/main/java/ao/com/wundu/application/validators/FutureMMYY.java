package ao.com.wundu.application.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FutureMMYYValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureMMYY {

    String message() default "Data de expiração deve ser futura";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
