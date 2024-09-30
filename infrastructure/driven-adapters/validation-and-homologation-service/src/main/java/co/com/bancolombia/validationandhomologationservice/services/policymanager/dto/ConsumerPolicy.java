package co.com.bancolombia.validationandhomologationservice.services.policymanager.dto;
import co.com.bancolombia.policymanager.dtos.response.Consumer;
import lombok.Builder;
import java.util.Optional;

@Builder(toBuilder = true)
public record ConsumerPolicy(String applicationId, String stiCode, String domainData, String interfaceName,
                             String sourceSystem) {


    public static ConsumerPolicy fromConsumer(Consumer consumer) {

        return ConsumerPolicy.builder()
                .applicationId(getValueOrEmpty(consumer.getApplication().getId()))
                .stiCode(getValueOrEmpty(consumer.getApplication().getStiCode()))
                .domainData(getValueOrEmpty(consumer.getDataDomain()))
                .interfaceName(getValueOrEmpty(consumer.getInterfaceName()))
                .sourceSystem(getValueOrEmpty(consumer.getSourceSystem()))
                .build();
    }

    private static String getValueOrEmpty(String value) {
        return Optional.ofNullable(value).orElse("");
    }



}
