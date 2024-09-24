package co.com.bancolombia.iastpatternconfig.config.json_dto.data_types;

import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqJsonValue;
import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqValue;
import co.com.bancolombia.iastpatternconfig.config.utils.FillDirection;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;
import java.util.stream.Collectors;
import static co.com.bancolombia.iastpatternconfig.config.utils.JsonUtils.*;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.ARRAY;
import static co.com.bancolombia.model.validation.ValidationService.validate;

@Getter
public class ArrayReqJsonValue extends ReqJsonValue {

    private final String expression;

    private final ReqValue arrayReqValue;


    @JsonCreator
    public ArrayReqJsonValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("length") int length,
            @JsonProperty("optional") boolean optional,
            @JsonProperty("fillValue") String fillValue,
            @JsonProperty("fillDirection") FillDirection fillDirection,
            @JsonProperty("expression") String expression,
            @JsonProperty("arrayValue") ReqValue arrayReqValue
    ) {
        super(attribute, length, optional, fillValue, fillDirection);
        validate(expression, StringUtils::isEmpty, "Invalid expression, it cannot be empty");

        this.arrayReqValue = arrayReqValue;
        this.expression = expression;
    }

    @Override
    public Type getType() {
        return ARRAY;
    }

    @Override
    public String getValue(JsonNode jsonNode) {


        var arrayNode = getArrayValueOfJson(attribute, jsonNode, optional);
        var value = buildStringFromArray(arrayNode ,this.expression);

        // Realiza el fillValue a nivel de concatenación final !!
        return getValue(value);

    }

    @Override
    public String getValue(String fieldValue) {
        return fillValue(fieldValue, this);
    }

    // Falta logica, hay que hacer switch interno

    @SneakyThrows
    private String buildStringFromArray(JsonNode jsonArrayNode, String expression) {

        ObjectMapper objectMapper = new ObjectMapper();


        String jsonAsString = objectMapper.writeValueAsString(jsonArrayNode);
        //var consultationResult  = JsonPath.<List<Map<String, Object>>>read(jsonAsString, expression);
        JSONArray consultationResult  = JsonPath.read(jsonAsString, expression);

        var isMap = consultationResult.stream().allMatch( rta -> rta instanceof Map);

        return isMap ? consultationResult.stream()
                .map(item -> ((Map<?, ?>) item)
                        .values().stream()
                        .map(value -> this.arrayReqValue != null && this.arrayReqValue instanceof ReqJsonValue ?
                                ((ReqJsonValue) this.arrayReqValue).getValue(value.toString()) : value.toString()) // Usar fillValue solo si subAttribute no es null
                        .collect(Collectors.joining()))
                .collect(Collectors.joining()) :
                consultationResult.stream()
                        .map(value -> this.arrayReqValue != null && this.arrayReqValue instanceof ReqJsonValue ?
                                ((ReqJsonValue) this.arrayReqValue).getValue(value.toString()) : value.toString()).collect(Collectors.joining()) ;


    }


}
