package co.com.bancolombia.iastpatternconfig.config.utils;

import co.com.bancolombia.model.validation.exceptions.BusinessDetailException;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;

import static co.com.bancolombia.model.validation.exceptions.message.BusinessErrorMessage.VALIDATION_DATA_ERROR;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class HomologationUtils {

    public static String getHomologationValue(String value, String typology, Map<String, String> equivalences) {
        var key = typology + "-" + value;
        return Optional.ofNullable(equivalences.get(key)).orElseThrow(() ->
                new BusinessDetailException(VALIDATION_DATA_ERROR, String.format("%s: homologation not found", key)));
    }
}
