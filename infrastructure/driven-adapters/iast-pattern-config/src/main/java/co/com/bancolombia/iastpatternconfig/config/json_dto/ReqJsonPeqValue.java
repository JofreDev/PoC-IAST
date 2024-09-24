package co.com.bancolombia.iastpatternconfig.config.json_dto;

import co.com.bancolombia.iastpatternconfig.config.utils.FillDirection;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static co.com.bancolombia.model.validation.ValidationService.validate;

public abstract class ReqJsonPeqValue extends ReqValue{

    protected final String attribute;
    protected final String typology;

    public ReqJsonPeqValue(String attribute,int length, boolean optional, String fillValue,
                           FillDirection fillDirection, String typology) {
        super(length, optional, fillValue, fillDirection);
        validate(attribute, StringUtils::isEmpty, "Invalid attribute");
        validate(typology, StringUtils::isEmpty, "Invalid typology");
        this.attribute = "/" + attribute.replace(".", "/");
        this.typology = typology;
    }


    public abstract String getHomologatedValue(JsonNode jsonNode, Map<String, String> equivalences);
}
