package co.com.bancolombia.iastpatternconfig;

import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqInnerValue;
import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqJsonPeqValue;
import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqJsonValue;
import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqValue;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResInnerValue;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResPlainPeqValue;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResPlainValue;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResValue;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.data_types.ArrayResPlainValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransformService {

    // Valida si una aprte del texto es numerica (entera)
    public static final String NUMBER_REGEX = "^\\d+$";
    // Valida la existencia de puntos
    public static final String DOT_REGEX = "\\.";


    /**
     * GenerateRequest
     */
    public static String generateRequest(JsonNode json, List<ReqValue> attributeModels,
                                         Map<String, String> equivalences) {

        return attributeModels.stream().map(attribute -> {
                return attribute instanceof ReqJsonValue
                        ? ((ReqJsonValue) attribute).getValue(json)
                        : attribute instanceof ReqInnerValue
                        ? ((ReqInnerValue) attribute).getValue()
                        : ((ReqJsonPeqValue) attribute).getHomologatedValue(json,equivalences);

        }).collect(Collectors.joining());
    }


    /**
     * GenerateResponse
     */

    public static JsonNode generateResponse(
            String response, List<ResValue> attributeResponses, Map<String, String> equivalences) {

        JsonNode rootNode = getRootNode(attributeResponses);

        attributeResponses.forEach(attribute -> {
            Optional<List<Object>> optionalValues = Optional.ofNullable(
                    attribute instanceof ArrayResPlainValue ? ((ArrayResPlainValue) attribute).getValues(response) : null
            );
            optionalValues.ifPresentOrElse(values -> {
                Object firstElement = values.stream().findFirst().orElse(null);

                Optional<Object> firstElementOpt = Optional.ofNullable(firstElement);

                firstElementOpt.filter(Map.class::isInstance)
                        .ifPresentOrElse(firstMap ->
                                        IntStream.range(0, values.size()).forEach(i -> {
                                            Map<String, String> map = (Map<String, String>) values.get(i);
                                            map.forEach((key, value) -> {
                                                String path = attribute.getAttribute().concat("." + i + "." + key);
                                                putNode(value, path, rootNode);
                                            });
                                        }),
                                () -> IntStream.range(0, values.size()).forEach(i -> {
                                    String path = attribute.getAttribute().concat("." + i);
                                    putNode((String) values.get(i), path, rootNode);
                                })
                        );
            }, () -> {
                // Manejo para los otros tipos de ResValue
                String value = getValueForAttribute(attribute, response, equivalences);
                putNode(value, attribute.getAttribute(), rootNode);
            });
        });

        return rootNode;
    }

    private static String getValueForAttribute(ResValue attribute, String response, Map<String, String> equivalences) {
        return

                Optional.ofNullable(attribute)
                        .map(attr -> {
                            return attr instanceof ResPlainValue
                                    ? ((ResPlainValue) attr).getValue(response)
                                    : attr instanceof ResInnerValue
                                    ? ((ResInnerValue) attr).getValue()
                                    : (attr instanceof ResPlainPeqValue)
                                    ? ((ResPlainPeqValue) attr).getHomologatedValue(response, equivalences)
                                    : null;
                        }).orElse(null);
    }



    private static void  putNode(String value, String path, JsonNode root) {
        String[] splitPath = path.split(DOT_REGEX);
        int targetPosition = splitPath.length - 1;
        JsonNode rootNode = root;

        for (int i = 0; i < splitPath.length; i++) {

            String currentKey = splitPath[i];
            boolean isCurrentNumber = isNumber(currentKey);
            boolean isNextNumber = (i + 1 < splitPath.length) && isNumber(splitPath[i + 1]);

            JsonNode currentNode = isCurrentNumber ? rootNode.path(Integer.parseInt(currentKey)) : rootNode.path(currentKey);

            if (i < targetPosition && currentNode.isMissingNode()) {

                if (isNextNumber) {
                    rootNode = isCurrentNumber ? ((ArrayNode) rootNode).addArray() : ((ObjectNode) rootNode).putArray(currentKey);
                } else {
                    rootNode = isCurrentNumber ? ((ArrayNode) rootNode).addObject() : ((ObjectNode) rootNode).putObject(currentKey);
                }
            } else if (i == targetPosition) {
                if (rootNode.isObject()) {
                    ((ObjectNode) rootNode).put(currentKey, value);
                } else if (rootNode.isArray()) {
                    ((ArrayNode) rootNode).add(value);
                }
            }else{
                rootNode =currentNode;
            }
        }

    }



    private static JsonNode getRootNode(List<ResValue> attributeResponses) {

        var optional = attributeResponses.stream()
                // Se obtiene la raiz para saber la estructura general
                .findFirst()
                .flatMap(plainValue -> Stream.of(plainValue.getAttribute().split("\\.")).findFirst())
                // validación -> ¿Array o JsonNode normal?
                .map(attribute -> isNumber(attribute) ? JsonNodeFactory.instance.arrayNode()
                        : JsonNodeFactory.instance.objectNode());

        return optional.orElseGet(JsonNodeFactory.instance::objectNode);
    }

    private static boolean isNumber(String value) {
        return value.matches(NUMBER_REGEX);
    }


}
