package co.com.bancolombia.iastpatternconfig.config.plaintext_dto.data_types;

import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResPlainValue;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import static co.com.bancolombia.iastpatternconfig.config.utils.ExpressionUtils.getExpressionValue;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.EXPRESSION;
import static co.com.bancolombia.model.validation.ValidationService.validate;

@Getter
public class ExpressionResPlainValue extends ResPlainValue {

    private final String expression;

    public ExpressionResPlainValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("initPosition") int initPosition,
            @JsonProperty("length") int length,
            @JsonProperty("expression") String expression
    ) {
        super(attribute, initPosition, length);

        validate(expression, StringUtils::isEmpty, "Invalid expression");
        this.expression = expression;
    }

    @Override
    public Type getType() {
        return EXPRESSION;
    }

    @Override
    public String getValue(String plainText) {
        var valueOfPlain =  plainText.substring(initPosition, initPosition + length).trim();
        return getExpressionValue(valueOfPlain, expression);
    }

    @Override
    public String getFinalValue(String finalValue) {
        return getExpressionValue(finalValue, expression);
    }
}
