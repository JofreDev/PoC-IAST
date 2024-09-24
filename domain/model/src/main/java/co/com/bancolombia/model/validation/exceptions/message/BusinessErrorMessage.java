package co.com.bancolombia.model.validation.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessErrorMessage {
    VALIDATION_DATA_ERROR("BP9001", "Validation data error"),
    CONSUMER_DOES_NOT_HAVE_PERMISSION("BP9002", "Consumer doesn't have permission"),
    CONSUMER_ID_NOT_FOUND("BP9003", "ConsumerId not registered in cmsti catalog policy"),
    CONSUMER_ID_NOT_FOUND_FOR_CONTINGENCY(
            "BP9004", "ConsumerId not registered in contingency catalog policy");

    private final String code;
    private final String message;
}