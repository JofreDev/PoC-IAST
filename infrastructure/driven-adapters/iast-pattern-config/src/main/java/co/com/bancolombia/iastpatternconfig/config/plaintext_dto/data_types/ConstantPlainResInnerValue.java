package co.com.bancolombia.iastpatternconfig.config.plaintext_dto.data_types;

import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResInnerValue;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import static co.com.bancolombia.iastpatternconfig.config.utils.Type.CONSTANT;
import static co.com.bancolombia.model.validation.ValidationService.validate;

@Getter
public class ConstantPlainResInnerValue extends ResInnerValue {

    private final String constant;

    public ConstantPlainResInnerValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("constant") String constant
    ) {
        super(attribute, 0, 0);

        validate(constant, StringUtils::isEmpty, "Invalid constant");
        this.constant = constant;
    }

    @Override
    public Type getType() {
        return CONSTANT;
    }

    @Override
    public String getValue() {
        return constant;
    }
}