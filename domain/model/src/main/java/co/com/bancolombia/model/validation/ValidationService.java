package co.com.bancolombia.model.validation;

import co.com.bancolombia.model.validation.exceptions.BusinessDetailException;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static co.com.bancolombia.model.validation.exceptions.message.BusinessErrorMessage.VALIDATION_DATA_ERROR;

public class ValidationService {

    private ValidationService() {}

    public static <T> void validate(T objeto, Predicate<T> predicate, String error) {
        if (predicate.test(objeto)) {
            throw new BusinessDetailException(VALIDATION_DATA_ERROR, error);
        }
    }

    public static <T> void validate(T objeto, Predicate<T> predicate, Supplier<String> error) {
        if (predicate.test(objeto)) {
            throw new BusinessDetailException(VALIDATION_DATA_ERROR, error.get());
        }
    }
}
