package co.com.bancolombia.iastpatternconfig.config.utils;
import co.com.bancolombia.iastpatternconfig.config.json_dto.*;
import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqValue;
import co.com.bancolombia.model.validation.exceptions.BusinessDetailException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static co.com.bancolombia.model.validation.exceptions.message.BusinessErrorMessage.VALIDATION_DATA_ERROR;

/*Realizar logica de la ruta valida hasta x puntok*/
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JsonUtils {

    /*Revisar caso de listas*/

    /*Esta estrategia mapea directamente un valor con un campo
    * El caso de listas y objetos se debe revisar y añadir en caso
    * que se requiera*/

    public static String getValueOfJson(String attribute, JsonNode json, boolean optional) {
        var node = json.at(attribute);
        // Revisar esta opcionalidad !!
        if (node.isMissingNode() && optional) {
            return "";
        }
        if (node.isMissingNode()) {
            throw new BusinessDetailException(
                    VALIDATION_DATA_ERROR, String.format("%s: parameter not found", attribute));
        }
        // No puede ser más que un campo primitivo !!
        if (!node.isValueNode() ) {
            throw new BusinessDetailException(
                    VALIDATION_DATA_ERROR, String.format("%s: is not a value node", attribute));
        }

        // añadir el caso en el que sea un arreglo en que llegar
        // tener en cuenta que se puede elegir solo ciertos atributos

        return node.asText();
    }

    public static JsonNode getArrayValueOfJson(String attribute, JsonNode json, boolean optional) {
        var node = json.at(attribute);
        // Revisar esta opcionalidad !!
        /*if (node.isMissingNode() && optional) {
            return "";
        }*/
        if (node.isMissingNode()) {
            throw new BusinessDetailException(
                    VALIDATION_DATA_ERROR, String.format("%s: parameter not found", attribute));
        }

        if (!node.isArray() ) {
            throw new BusinessDetailException(
                    VALIDATION_DATA_ERROR, String.format("%s: is not a array node", attribute));
        }


        return node;
    }

    public static String fillValue(String textValue, ReqJsonValue attribute) {
        // La longitud del campo(JsonValue) debe ser menor a longitud del campo en trama

        if (textValue.length() > attribute.getLength()) {
            throw new BusinessDetailException(VALIDATION_DATA_ERROR,
                    String.format("%s: invalid lenght (%s)",   attribute.getType(), attribute.getLength()));
        }

        return switch (attribute.getFillDirection()) {
            // Completa campo con caracteres a la derecha
            case RIGHT -> StringUtils.rightPad(textValue, attribute.getLength(), attribute.getFillValue());
            // Completa campo con caracteres a la izquierda
            default -> StringUtils.leftPad(textValue, attribute.getLength(), attribute.getFillValue());
        };
    }

    public static String fillValue(String textValue, ReqValue attribute) {
        // La longitud del campo(JsonValue) debe ser menor a longitud del campo en trama

        if (textValue.length() > attribute.getLength()) {
            throw new BusinessDetailException(VALIDATION_DATA_ERROR,
                    String.format("%s: invalid lenght (%s)",   attribute.getType(), attribute.getLength()));
        }


        return switch (attribute.getFillDirection()) {
            // Completa campo con caracteres a la derecha
            case RIGHT -> StringUtils.rightPad(textValue, attribute.getLength(), attribute.getFillValue());
            // Completa campo con caracteres a la izquierda
            default -> StringUtils.leftPad(textValue, attribute.getLength(), attribute.getFillValue());
        };
    }



}