package co.com.bancolombia.iastpatternconfig.config;

import co.com.bancolombia.iastpatternconfig.config.json_dto.ReqValue;
import co.com.bancolombia.iastpatternconfig.config.plaintext_dto.ResValue;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import static co.com.bancolombia.model.validation.ValidationService.validate;

public record PatternModel(
        String name, String request, List<ReqValue> requestModel,
        List<ResValue> responseModels, int responseLength
) {

    public PatternModel(String name, String request, List<ReqValue> requestModel,
                        List<ResValue> responseModels, int responseLength) {

        validate(name, StringUtils::isEmpty, "Name is invalid");
        //validate(request, StringUtils::isEmpty, "Request is invalid");
        validate(requestModel, rm -> rm == null || rm.isEmpty(), "RequestModel is invalid");
        validate(responseModels, rm -> rm == null || rm.isEmpty(), "ResponseModel is invalid");
/*
        var minimumLength = responseModels.stream().max(Comparator.comparing(PlainValue::getInitPosition))
                .map(responseModel -> responseModel.getInitPosition() + responseModel.getLength())
                .orElseThrow(() -> new TechnicalException(INVALID_CONFIGURATION));
*/
        this.name = name;
        this.request = request;
        this.requestModel = requestModel;
        this.responseModels = responseModels;
        this.responseLength = responseLength;
    }
}
