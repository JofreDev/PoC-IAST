package co.com.bancolombia.iastpatternconfig.config.json_dto;
import co.com.bancolombia.iastpatternconfig.config.utils.FillDirection;

public abstract class ReqInnerValue extends ReqValue {
    public ReqInnerValue(int length, boolean optional, String fillValue, FillDirection fillDirection) {
        super(length, optional, fillValue, fillDirection);
    }

    // Genera exclusivamente datos de manera interna!!
    public abstract String getValue();
}
