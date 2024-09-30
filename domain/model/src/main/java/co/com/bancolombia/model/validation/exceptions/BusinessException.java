package co.com.bancolombia.model.validation.exceptions;

import co.com.bancolombia.model.validation.exceptions.message.BusinessErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private BusinessErrorMessage bussinesErrorMessage;

    private String code;
    private String messageError;
    private Integer status;

    public BusinessException(BusinessErrorMessage bussinesErrorMessage) {
        super(bussinesErrorMessage.getMessage());
        this.bussinesErrorMessage = bussinesErrorMessage;
    }

    public BusinessException(String code, String message, Integer status) {
        super(message);
        this.code = code;
        this.messageError = message;
        this.status = status;
    }
}