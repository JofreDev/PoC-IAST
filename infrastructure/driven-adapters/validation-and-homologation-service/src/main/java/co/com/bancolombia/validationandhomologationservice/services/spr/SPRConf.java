package co.com.bancolombia.validationandhomologationservice.services.spr;

import co.com.bancolombia.spr.config.SPRConsumerConfig;
import co.com.bancolombia.spr.infraestructure.SPRConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class SPRConf {

    @Bean
    public SPRConsumer SPRConfig(){

        return new SPRConsumer(SPRConsumerConfig.builder()
                .uri(URI.create("https://integracion-int-qa.apps.ambientesbc.lab/integration-common/spr-adapter-camel/equivalenceResponseCode"))
                .cacheExpireAfter(3600)
                .build()
        );
    }
}
