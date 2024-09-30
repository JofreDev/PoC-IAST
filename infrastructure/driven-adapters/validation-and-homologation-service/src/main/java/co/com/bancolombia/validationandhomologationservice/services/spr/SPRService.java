package co.com.bancolombia.validationandhomologationservice.services.spr;

import co.com.bancolombia.spr.domain.model.EquivalenceRequestData;
import co.com.bancolombia.spr.domain.model.EquivalenceResponse;
import co.com.bancolombia.spr.infraestructure.SPRConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SPRService {
    private final SPRConsumer sprConsumer;

    public Flux<EquivalenceResponse> getAndSaveONCache(String codigoProveedor, String codigoRespuestaProveedor, String estadoRespuesta, String codigoIdioma, String codigoCanal){

        List<EquivalenceRequestData> dataList = new ArrayList<>();
        dataList.add(new EquivalenceRequestData(codigoProveedor, codigoRespuestaProveedor, estadoRespuesta, codigoIdioma, codigoCanal));

        return sprConsumer.getAndSaveOnCache(dataList);
    }
}
