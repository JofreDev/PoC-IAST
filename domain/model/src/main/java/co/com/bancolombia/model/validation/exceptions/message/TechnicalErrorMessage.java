package co.com.bancolombia.model.validation.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalErrorMessage {

    //INVALID_CONFIGURATION("SP9101", "Invalid configuration"),
    //EQUIVALENCE_SERVICE_ERROR("SP9102", "Equivalence service error"),
    POLICY_MANAGER_SERVICE_ERROR("SP9103", "Policy manager service error",300,""),
    INVALID_EXPRESSION("SP9110", "Invalid expression",300,""),
    INVALID_QUERY_EXPRESSION("SP9110", "Invalid query expression in json",300,""),
    INVALID_NUMBER("SP9111", "Invalid number",300,""),
    INVALID_DATE("SP9112", "Invalid date",300,"");

    private final String code;
    private final String message;
    private final Integer status;
    private final String title;
}
