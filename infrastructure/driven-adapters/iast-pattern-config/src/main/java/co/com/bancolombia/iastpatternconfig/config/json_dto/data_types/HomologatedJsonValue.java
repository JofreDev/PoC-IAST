package co.com.bancolombia.iastpatternconfig.config.json_dto.data_types;
import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqJsonPeqValue;
import co.com.bancolombia.iastpatternconfig.config.utils.FillDirection;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import java.util.Map;
import static co.com.bancolombia.iastpatternconfig.config.utils.HomologationUtils.getHomologationValue;
import static co.com.bancolombia.iastpatternconfig.config.utils.JsonUtils.fillValue;
import static co.com.bancolombia.iastpatternconfig.config.utils.JsonUtils.getValueOfJson;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.HOMOLOGATION;

@Getter
public class HomologatedJsonValue extends ReqJsonPeqValue {

    public HomologatedJsonValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("length") int length,
            @JsonProperty("optional") boolean optional,
            @JsonProperty("fillValue") String fillValue,
            @JsonProperty("fillDirection") FillDirection fillDirection,
            @JsonProperty("typology") String typology
    ) {
        super(attribute, length, optional, fillValue, fillDirection, typology);

    }

    @Override
    public Type getType() {
        return HOMOLOGATION;
    }


    @Override
    public String getHomologatedValue(JsonNode jsonNode, Map<String, String> equivalences) {
        var valueOfJson = getValueOfJson(attribute, jsonNode, optional);
        var value = optional && valueOfJson.isEmpty() ? valueOfJson :
                getHomologationValue(valueOfJson, typology, equivalences);
        return fillValue(value, this);
    }
}
