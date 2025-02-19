package lt.ca.javau11.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Year;

public class YearValidator implements ConstraintValidator<ValidYear, Integer> {
    private static final int MIN_YEAR = 1900;

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return false;
        }
        int currentYear = Year.now().getValue();
        return year >= MIN_YEAR && year <= currentYear;
    }
}