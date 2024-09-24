package co.com.bancolombia.iastpatternconfig.config.json_dto.data_types;
import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqInnerValue;
import co.com.bancolombia.iastpatternconfig.config.utils.FillDirection;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import static co.com.bancolombia.iastpatternconfig.config.utils.ExpressionUtils.getExpressionValue;
import static co.com.bancolombia.iastpatternconfig.config.utils.JsonUtils.fillValue;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.GENERATED;
import static co.com.bancolombia.model.validation.ValidationService.validate;

@Getter
public class GeneratedJsonValueReq extends ReqInnerValue {
    private final String expression;

    public GeneratedJsonValueReq(
            @JsonProperty("length") int length,
            @JsonProperty("fillValue") String fillValue,
            @JsonProperty("fillDirection") FillDirection fillDirection,
            @JsonProperty("expression") String expression
    ) {
        super(length, false, fillValue, fillDirection);

        validate(expression, StringUtils::isEmpty, "Invalid expression");
        this.expression = expression;
    }

    @Override
    public Type getType() {
        return GENERATED;
    }
    @Override
    public String getValue() {
        // Es vacio porque acá la expresión es para generar un dato no para validar
        var value = getExpressionValue("EMPTY", expression);
        return fillValue(value, this);
    }
}
