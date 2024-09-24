package co.com.bancolombia.iastpatternconfig.config.plaintext_dto;


import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.data_types.*;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import static co.com.bancolombia.model.validation.ValidationService.validate;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValueResPlainValue.class, name = "VALUE"),
        @JsonSubTypes.Type(value = NumericResPlainValue.class, name = "NUMBER"),
        @JsonSubTypes.Type(value = ConstantPlainResInnerValue.class, name = "CONSTANT"),
        @JsonSubTypes.Type(value = GeneratedPlainResInnerValue.class, name = "GENERATED"),
        @JsonSubTypes.Type(value = ExpressionResPlainValue.class, name = "EXPRESSION"),
        @JsonSubTypes.Type(value = ArrayResPlainValue.class, name = "ARRAY")
})
public abstract class ResValue {

    protected final String attribute;
    protected final int initPosition;
    protected final int length;

    protected ResValue(String attribute, int initPosition, int length) {
        validate(attribute, StringUtils::isEmpty, "Invalid attribute");
        validate(initPosition, l -> l < 0, "Invalid initPosition");
        validate(length, l -> l < 0, "Invalid length");

        this.attribute = attribute;
        this.initPosition = initPosition;
        this.length = length;
    }

    public abstract Type getType();

}
