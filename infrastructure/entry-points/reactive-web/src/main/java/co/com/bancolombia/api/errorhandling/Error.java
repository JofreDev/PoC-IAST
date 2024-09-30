package co.com.bancolombia.api.errorhandling;

import lombok.Builder;

@Builder
public record Error(String code, String detail) {

}
