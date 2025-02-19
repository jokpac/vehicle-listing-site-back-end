package lt.ca.javau11.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YearValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYear {
    String message() default "Year must be between 1900 and the current year";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}