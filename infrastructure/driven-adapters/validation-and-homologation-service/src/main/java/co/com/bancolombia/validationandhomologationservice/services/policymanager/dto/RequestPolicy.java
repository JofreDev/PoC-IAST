package co.com.bancolombia.validationandhomologationservice.services.policymanager.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record RequestPolicy(String namespace, String applicationId, String operationId, String messageId){ }
