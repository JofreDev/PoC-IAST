package co.com.bancolombia.iastpatternconfig.config.utils;

import co.com.bancolombia.model.validation.exceptions.TechnicalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import org.springframework.expression.ExpressionException;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.model.validation.exceptions.message.TechnicalErrorMessage.INVALID_EXPRESSION;
import static co.com.bancolombia.model.validation.exceptions.message.TechnicalErrorMessage.INVALID_QUERY_EXPRESSION;

public class JsonPathExpressionsUtils {

    @SneakyThrows
    public static List<Object> getJsonExpressionValue(JsonNode jsonArrayNode, String expression) {

        ObjectMapper objectMapper = new ObjectMapper();
        var jsonAsString = objectMapper.writeValueAsString(jsonArrayNode);
        try {
            return JsonPath.read(jsonAsString, expression);
        } catch (Exception exception) {
            throw new TechnicalException(INVALID_QUERY_EXPRESSION, exception);
        }
    }


}
