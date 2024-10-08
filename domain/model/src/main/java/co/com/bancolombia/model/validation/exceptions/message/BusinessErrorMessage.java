package co.com.bancolombia.model.validation.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessErrorMessage {
    VALIDATION_DATA_ERROR("BP9001", "Validation data error",300,""),
    CONSUMER_DOES_NOT_HAVE_PERMISSION("BP9002", "Consumer doesn't have permission",300,"");

    private final String code;
    private final String message;
    private final Integer status;
    private final String title;

}