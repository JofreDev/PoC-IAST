package co.com.bancolombia.iastpatternconfig.config.plaintext_dto;

public abstract class ResInnerValue extends ResValue{
    protected ResInnerValue(String attribute, int initPosition, int length) {
        super(attribute, initPosition, length);
    }

    public abstract String getValue();
}
