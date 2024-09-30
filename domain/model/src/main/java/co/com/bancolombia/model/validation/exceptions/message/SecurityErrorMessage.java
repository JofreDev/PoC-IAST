package co.com.bancolombia.model.validation.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityErrorMessage {


    CONSUMER_NOT_ALLOWED("SEC8000", "Application is not an authorized consumer (Policy Manager)",403, "Not authorized");

    private final String code;
    private final String message;
    private final Integer status;
    private final String title;
}
