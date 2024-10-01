package co.com.bancolombia.loggingandmonitoringservice.loggingadapter;

import co.com.bancolombia.domain.models.enums.EventType;
import co.com.bancolombia.kafka.loggin.infraestructure.kafkaproducer.LogginAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoggingAdapterTest {
    @Mock
    private LogginAdapter kafkaProducer;
    KafkaLogEventSender kafkaLogEventSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaLogEventSender = new KafkaLogEventSender(kafkaProducer);

    }

    @Test
    public void loggingAdapterEventTest(){

        String body="{\"data\":{\"numeroCelular\":\"3243298536\",\"tipoDocumento\":\"C\",\"identificadorValidacion\":\"3\",\"numeroValidaciones\":\"55\",\"codigoTransaccion\":\"0222\",\"valor\":\"200000\",\"canal\":\"APP\",\"numeroDocumento\":\"9909090754\",\"mensaje\":\"Prueba de mensaje\",\"identificadorTransaccion\":\"00000000000\",\"correoElectronico\":\"kevmarti@bancolombia.com.co\"}}";

        when(kafkaProducer.loggingAdapterEvent(any(),any(),any(),any(),any(),any(),any(),any(),any(), any())).thenReturn(Mono.empty());

        kafkaLogEventSender.loggingAdapter( EventType.REQUEST, "messageId","consumerId","operation", body)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void loggingAdapterErrorEventTest(){

        String body="{\"data\":{\"numeroCelular\":\"3243298536\",\"tipoDocumento\":\"C\",\"identificadorValidacion\":\"3\",\"numeroValidaciones\":\"55\",\"codigoTransaccion\":\"0222\",\"valor\":\"200000\",\"canal\":\"APP\",\"numeroDocumento\":\"9909090754\",\"mensaje\":\"Prueba de mensaje\",\"identificadorTransaccion\":\"00000000000\",\"correoElectronico\":\"kevmarti@bancolombia.com.co\"}}";

        when(kafkaProducer.loggingAdapterErrorEvent(any(),any(),any(),any(),any(),any(), any(), any(),any(), any())).thenReturn(Mono.empty());

        kafkaLogEventSender.loggingErrorAdapter( "messageId","consumerId",body,null, null)
                .as(StepVerifier::create)
                .verifyComplete();
    }


    @Test
    public void loggingAdapterHTTPEventTest(){

        String body="{\"data\":{\"numeroCelular\":\"3243298536\",\"tipoDocumento\":\"C\",\"identificadorValidacion\":\"3\",\"numeroValidaciones\":\"55\",\"codigoTransaccion\":\"0222\",\"valor\":\"200000\",\"canal\":\"APP\",\"numeroDocumento\":\"9909090754\",\"mensaje\":\"Prueba de mensaje\",\"identificadorTransaccion\":\"00000000000\",\"correoElectronico\":\"kevmarti@bancolombia.com.co\"}}";

        when(kafkaProducer.loggingAdapterEvent(any(),any(), any(), any(),any(), any(), any(), any(), any(), any())).thenReturn(Mono.empty());

        kafkaLogEventSender.loggingHttpAdapter( EventType.RESPONSE,  "messageId","consumerId","operation", body, null,  "POST",  200)
                .as(StepVerifier::create)
                .verifyComplete();
    }


    @Test
    public void loggingAdapterHTTPErrorEventTest(){

        String body="{\"data\":{\"numeroCelular\":\"3243298536\",\"tipoDocumento\":\"C\",\"identificadorValidacion\":\"3\",\"numeroValidaciones\":\"55\",\"codigoTransaccion\":\"0222\",\"valor\":\"200000\",\"canal\":\"APP\",\"numeroDocumento\":\"9909090754\",\"mensaje\":\"Prueba de mensaje\",\"identificadorTransaccion\":\"00000000000\",\"correoElectronico\":\"kevmarti@bancolombia.com.co\"}}";

        when(kafkaProducer.loggingAdapterErrorEvent(any(),any(), any(), any(),any(), any(), any(), any(), any(), any())).thenReturn(Mono.empty());

        kafkaLogEventSender.loggingHttpErrorAdapter("messageId","consumerId","operation", body, null, "POST", 500, null)
                .as(StepVerifier::create)
                .verifyComplete();
    }

}
