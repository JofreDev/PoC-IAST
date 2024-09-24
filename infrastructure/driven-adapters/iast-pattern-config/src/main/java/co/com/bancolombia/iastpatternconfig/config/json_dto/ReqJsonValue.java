package co.com.bancolombia.iastpatternconfig.config.json_dto;

import co.com.bancolombia.iastpatternconfig.config.utils.FillDirection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import static co.com.bancolombia.model.validation.ValidationService.validate;

@Getter
public abstract class ReqJsonValue extends ReqValue {

    protected final String attribute;

    public ReqJsonValue(String attribute, int length, boolean optional, String fillValue, FillDirection fillDirection) {
        super(length, optional, fillValue, fillDirection);
        validate(attribute, StringUtils::isEmpty, "Invalid attribute");
        this.attribute = "/" + attribute.replace(".", "/");
    }

    // Se puede añadir logica o acuerdos exclusivos para los tipos que serán mapeados con el JSON

    // Ejecuta logica de mapeo de datos exclusivamente con el jSON entrante !!
    public abstract String getValue(JsonNode jsonNode);

    // Ejecuta logica de transformación por tipo de dato primitivo en los casos ARRAY !!
    public abstract String getValue(String fieldValue);

}
