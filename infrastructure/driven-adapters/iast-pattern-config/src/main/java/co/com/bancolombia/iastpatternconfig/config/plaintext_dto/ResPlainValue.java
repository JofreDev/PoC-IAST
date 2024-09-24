package co.com.bancolombia.iastpatternconfig.config.plaintext_dto;

import static co.com.bancolombia.model.validation.ValidationService.validate;


public abstract class ResPlainValue extends ResValue{

    protected ResPlainValue(String attribute, int initPosition, int length) {
        super(attribute, initPosition, length);
    }

    public abstract String getValue(String plainText);

    public abstract String getFinalValue(String finalValue);
}
