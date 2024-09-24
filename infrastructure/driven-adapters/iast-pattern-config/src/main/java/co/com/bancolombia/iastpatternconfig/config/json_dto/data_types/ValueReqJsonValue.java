package co.com.bancolombia.iastpatternconfig.config.json_dto.data_types;
import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqJsonValue;
import co.com.bancolombia.iastpatternconfig.config.utils.FillDirection;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import static co.com.bancolombia.iastpatternconfig.config.utils.JsonUtils.fillValue;
import static co.com.bancolombia.iastpatternconfig.config.utils.JsonUtils.getValueOfJson;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.VALUE;
import static co.com.bancolombia.model.validation.ValidationService.validate;

public class ValueReqJsonValue extends ReqJsonValue {


    @JsonCreator
    public ValueReqJsonValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("length") int length,
            @JsonProperty("optional") boolean optional,
            @JsonProperty("fillValue") String fillValue,
            @JsonProperty("fillDirection") FillDirection fillDirection
    ) {
        super(attribute, length, optional, fillValue, fillDirection);
        validate(length, l -> l < 0, "Invalid length");
    }

    @Override
    public Type getType() {
        return VALUE;
    }

    @Override
    public String getValue(JsonNode jsonNode) {
        var value =  getValueOfJson(attribute, jsonNode, optional);
        return fillValue(value, this);
    }

    @Override
    public String getValue(String fieldValue) {
        return fillValue(fieldValue, this);
    }
}
