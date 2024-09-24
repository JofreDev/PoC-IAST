package co.com.bancolombia.iastpatternconfig.config.plaintext_dto.data_types;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResPlainValue;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import static co.com.bancolombia.iastpatternconfig.config.utils.Type.VALUE;

public class ValueResPlainValue extends ResPlainValue {

    public ValueResPlainValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("initPosition") int initPosition,
            @JsonProperty("length") int length
    ) {
        super(attribute, initPosition, length);
    }

    @Override
    public Type getType() {
        return VALUE;
    }

    @Override
    public String getValue(String plain) {
        return plain.substring(initPosition, initPosition + length).trim();
    }

    @Override
    public String getFinalValue(String finalValue) {
        // Se puede añadir alguna logica de transformación !!
        return finalValue;
    }
}