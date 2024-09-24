package co.com.bancolombia.model.validation.exceptions;

import co.com.bancolombia.model.validation.exceptions.message.TechnicalErrorMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {
    private final TechnicalErrorMessage errorMessage;

    public TechnicalException(TechnicalErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public TechnicalException(TechnicalErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getMessage(), cause);
        this.errorMessage = errorMessage;
    }
}
