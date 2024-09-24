package co.com.bancolombia.iastpatternconfig.config.plaintext_dto;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static co.com.bancolombia.model.validation.ValidationService.validate;


// retirar Clase !!!
public abstract class ResPlainPeqValue extends ResValue{

    protected final String typology;


    protected ResPlainPeqValue(String attribute, int initPosition, int length, String typology) {
        super(attribute, initPosition, length);
        validate(typology, StringUtils::isEmpty, "Invalid typology");
        this.typology = typology;
    }

    public abstract String getHomologatedValue(String plainText, Map<String, String> equivalences);
}