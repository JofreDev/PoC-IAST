package co.com.bancolombia.validationandhomologationservice.services.policymanager;
import co.com.bancolombia.model.validation.exceptions.SecurityException;
import co.com.bancolombia.policymanager.dtos.response.Consumer;
import co.com.bancolombia.model.validation.exceptions.TechnicalException;
import co.com.bancolombia.policymanager.PolicyManagerConnectorAsync;
import co.com.bancolombia.policymanager.exceptions.InvalidConsumerException;
import co.com.bancolombia.policymanager.exceptions.PolicyManagerException;
import co.com.bancolombia.validationandhomologationservice.services.policymanager.dto.RequestPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.model.validation.exceptions.message.SecurityErrorMessage.CONSUMER_NOT_ALLOWED;
import static co.com.bancolombia.model.validation.exceptions.message.TechnicalErrorMessage.POLICY_MANAGER_SERVICE_ERROR;


@Component
@RequiredArgsConstructor
public class PolicyManagerService {

    private final PolicyManagerConnectorAsync policyManagerConnectorAsync;


    public Mono<Consumer> verifyConsumer(RequestPolicy requestPolicy){
        return policyManagerConnectorAsync.verifyConsumer(requestPolicy.namespace(), requestPolicy.applicationId(),requestPolicy.operationId(),requestPolicy.messageId())
                .flatMap(serviceCatalogResponse -> {
                    List<Consumer> consumers = serviceCatalogResponse.getDataResponse().getService().getConsumers();

                    return Flux.fromIterable(consumers)
                            .filter(consumer -> consumer.getApplication().getId().equals(requestPolicy.applicationId()))
                            .single();
                })
                .onErrorResume(error-> Mono.error( error instanceof InvalidConsumerException
                        ? new SecurityException(CONSUMER_NOT_ALLOWED)
                        : error instanceof PolicyManagerException
                        ? new TechnicalException(POLICY_MANAGER_SERVICE_ERROR)
                        : error));
    }
}
