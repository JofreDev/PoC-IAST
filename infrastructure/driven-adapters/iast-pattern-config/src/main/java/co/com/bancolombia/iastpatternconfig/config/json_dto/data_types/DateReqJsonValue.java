package co.com.bancolombia.iastpatternconfig.config.json_dto.data_types;


import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqJsonValue;
import co.com.bancolombia.iastpatternconfig.config.utils.FillDirection;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import static co.com.bancolombia.iastpatternconfig.config.utils.FormatUtils.getDateWithFormat;
import static co.com.bancolombia.iastpatternconfig.config.utils.JsonUtils.fillValue;
import static co.com.bancolombia.iastpatternconfig.config.utils.JsonUtils.getValueOfJson;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.DATE;
import static co.com.bancolombia.model.validation.ValidationService.validate;


public class DateReqJsonValue extends ReqJsonValue {

    private final String format;

    public DateReqJsonValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("length") int length,
            @JsonProperty("optional") boolean optional,
            @JsonProperty("fillValue") String fillValue,
            @JsonProperty("fillDirection") FillDirection fillDirection,
            @JsonProperty("format") String format
    ) {
        super(attribute, length, optional, fillValue, fillDirection);
        validate(format, StringUtils::isEmpty, "Invalid format");
        this.format = format;
    }

    @Override
    public Type getType() {
        return DATE;
    }

    @Override
    public String getValue(JsonNode jsonNode) {
        var valueOfJson = getValueOfJson(attribute, jsonNode, optional);
        var value = optional && valueOfJson.isEmpty() ? valueOfJson : getDateWithFormat(valueOfJson, format);
        return fillValue(value, this);
    }

    @Override
    public String getValue(String fieldValue) {
        var value = optional && fieldValue.isEmpty() ? fieldValue : getDateWithFormat(fieldValue, format);
        return fillValue(value, this);
    }
}
