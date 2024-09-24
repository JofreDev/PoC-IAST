package co.com.bancolombia.iastpatternconfig.config.plaintext_dto.data_types;

import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResPlainValue;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.NUMBER;
import static co.com.bancolombia.model.validation.ValidationService.validate;

public class NumericResPlainValue extends ResPlainValue {

    public static final String REGEX = "^0+(?!$)";
    private final int numberDecimals;

    public NumericResPlainValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("initPosition") int initPosition,
            @JsonProperty("length") int length,
            @JsonProperty("numberDecimals") int numberDecimals
    ) {
        super(attribute, initPosition, length);

        validate(numberDecimals, n -> n >= length, "Invalid numberDecimals");
        this.numberDecimals = numberDecimals;
    }

    @Override
    public Type getType() {
        return NUMBER;
    }

    @Override
    public String getValue(String plainText) {
        var valueOfPlain =  plainText.substring(initPosition, initPosition + length).trim();

        var value = numberDecimals == 0 ? valueOfPlain : addDot(valueOfPlain);
        return value.replaceFirst(REGEX, "");
    }
    @Override
    public String getFinalValue(String finalValue) {
        var value = numberDecimals == 0 ? finalValue : addDot(finalValue);
        return value.replaceFirst(REGEX, "");
    }

    private String addDot(String valueOfPlain) {
        return new StringBuilder(valueOfPlain)
                .insert(valueOfPlain.length() - numberDecimals, ".")
                .toString();
    }
}
