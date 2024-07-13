package bg.codeacademy.cakeShop.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IbanValidator.class)
@Documented
public @interface ValidIban {

    String message() default "IBAN is not valid!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
