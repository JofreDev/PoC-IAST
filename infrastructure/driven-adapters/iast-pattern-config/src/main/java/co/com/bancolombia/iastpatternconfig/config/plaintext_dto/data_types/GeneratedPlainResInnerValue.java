package co.com.bancolombia.iastpatternconfig.config.plaintext_dto.data_types;

import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResInnerValue;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import static co.com.bancolombia.iastpatternconfig.config.utils.ExpressionUtils.getExpressionValue;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.GENERATED;
import static co.com.bancolombia.model.validation.ValidationService.validate;


@Getter
public class GeneratedPlainResInnerValue extends ResInnerValue {

    private final String expression;

    public GeneratedPlainResInnerValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("expression") String expression
    ) {
        super(attribute, 0, 0);

        validate(expression, StringUtils::isEmpty, "Invalid expression");
        this.expression = expression;
    }

    @Override
    public Type getType() {
        return GENERATED;
    }

    @Override
    public String getValue() {
        return getExpressionValue("EMPTY", expression);
    }
}
