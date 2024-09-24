package co.com.bancolombia.iastpatternconfig.config.plaintext_dto.data_types;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResPlainPeqValue;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;

import static co.com.bancolombia.iastpatternconfig.config.utils.HomologationUtils.getHomologationValue;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.HOMOLOGATION;
import static co.com.bancolombia.model.validation.ValidationService.validate;

@Getter
public class HomologatedPlainValue extends ResPlainPeqValue {


    protected HomologatedPlainValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("initPosition") int initPosition,
            @JsonProperty("length") int length,
            @JsonProperty("typology") String typology) {
        super(attribute, initPosition, length, typology);
    }


    @Override
    public Type getType() {
        return HOMOLOGATION;
    }

    @Override
    public String getHomologatedValue(String plainText, Map<String, String> equivalences) {
        var valueOfPlain =  plainText.substring(initPosition, initPosition + length).trim();
        return getHomologationValue(valueOfPlain, typology, equivalences);
    }

}
