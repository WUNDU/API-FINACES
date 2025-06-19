package ao.com.wundu.application.validators;

import ao.com.wundu.util.DateUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FutureMMYYValidator implements ConstraintValidator<FutureMMYY, String> {

    @Override
    public void initialize(FutureMMYY constraintAnnotation) {
        // Não precisa de inicialização específica
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Deixa a validação @NotNull/NotBlank tratar campos vazios
        }

        try {
            LocalDate expirationDate = DateUtils.convertMMyyToLocalDate(value);
            return DateUtils.isExpirationDateValid(expirationDate);
        } catch (IllegalArgumentException e) {
            // Se não conseguir converter, é inválido
            return false;
        }
    }
}
