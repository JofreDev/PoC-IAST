package co.com.bancolombia.iastpatternconfig.config.plaintext_dto.data_types;

import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResInnerValue;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResPlainValue;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResValue;
import co.com.bancolombia.iastpatternconfig.config.utils.Type;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static co.com.bancolombia.iastpatternconfig.config.utils.Type.ARRAY;
import static co.com.bancolombia.model.validation.ValidationService.validate;

public class ArrayResPlainValue extends ResValue {


    private final ArrayList<ResValue>  subAttributes;
    private final int limit;

    protected ArrayResPlainValue(
            @JsonProperty("attribute") String attribute,
            @JsonProperty("initPosition") int initPosition,
            @JsonProperty("length") int length,
            @JsonProperty("limit") int limit,
            @JsonProperty("subAttributes") ArrayList<ResValue> subAttributes
    ) {
        super(attribute, initPosition, length);


        validate(
                subAttributes,
                x -> x.size() != 1
                        && x.stream().findFirst().stream().allMatch(item -> item.getAttribute().isEmpty())
                        || x.size() > 1 && x.stream().allMatch(item -> item.getAttribute().isBlank()),
                "Invalid subattributes"
        );


        this.subAttributes = subAttributes;
        this.limit = limit;
    }

    @Override
    public Type getType() {
        return ARRAY;
    }

    public List<Object> getValues(String plainText) {
        String value = plainText.substring(initPosition, initPosition + length).trim();
        return subAttributes.size() == 1 && subAttributes.get(0).getAttribute().isBlank()
                ? createSimpleArray(value)
                : createObjectArray(value);
    }



    private List<Object> createSimpleArray(String finalValue) {
        ResValue arrayValue = subAttributes.stream().findFirst().get();
        int subInitPosition = arrayValue.getInitPosition();
        int subLength = arrayValue.getLength();

        return IntStream.iterate(subInitPosition, i -> i + subLength)
                .limit((finalValue.length() - subInitPosition + subLength - 1) / subLength)
                .mapToObj(i -> finalValue.substring(i, Math.min(i + subLength, finalValue.length())))
                .filter(subfield -> subfield.length() == subLength)  // Filtro para validar la longitud
                .map(value -> arrayValue instanceof ResPlainValue
                        ? ((ResPlainValue) arrayValue).getFinalValue(value)
                        : arrayValue instanceof ResInnerValue
                        ? ((ResInnerValue) arrayValue).getValue()
                        : value)
                .collect(Collectors.toList());
    }



    public  List<Object> createObjectArray(String finalValue) {
        List<Object> resultList = new LinkedList<>();

        int objectLength = this.subAttributes.stream().map(ResValue::getLength).reduce(Integer::sum).get();
        IntStream.iterate(this.subAttributes.stream().findFirst().get().getInitPosition(), i -> i + objectLength)
                .limit(limit) // En caso de que se defina un limite
                .forEach(i -> {
                    String subValue =  finalValue.substring(i, Math.min(i + objectLength, finalValue.length()));
                    Map<String, String> map = new LinkedHashMap<>();
                    this.subAttributes.forEach(x -> {
                        map.put(x.getAttribute(), (x instanceof ResPlainValue
                                ? ((ResPlainValue) x).getFinalValue(subValue.substring(x.getInitPosition(),x.getInitPosition()+x.getLength()))
                                : x instanceof ResInnerValue
                                ? ((ResInnerValue) x).getValue()
                                : subValue.substring(x.getInitPosition(),x.getInitPosition()+x.getLength())).trim());
                    });
                    resultList.add(map);
                });

        return resultList;  // Retornar la lista de mapas
    }


}
