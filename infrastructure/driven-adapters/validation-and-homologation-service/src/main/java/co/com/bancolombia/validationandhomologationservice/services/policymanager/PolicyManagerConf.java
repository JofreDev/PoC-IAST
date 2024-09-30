package co.com.bancolombia.validationandhomologationservice.services.policymanager;
import co.com.bancolombia.policymanager.PolicyManagerConnectorAsync;
import co.com.bancolombia.policymanager.config.PolicyManagerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class PolicyManagerConf {


    @Bean
    public PolicyManagerConnectorAsync policyManagerConfig(){
        return new PolicyManagerConnectorAsync(PolicyManagerConfig.builder()
                //.uri(URI.create("https://integracion-int-qa.apps.ambientesbc.com/service-catalog/policymanager/verifyConsumer"))
                .uri(URI.create("https://integracion-int-dev.apps.ambientesbc.com/service-catalog/policymanager/verifyConsumer"))
                .cacheExpireAfter(3600)
                .build()
        );
    }
}
