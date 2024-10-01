package co.com.bancolombia.loggingandmonitoringservice.loggingadapter;

import co.com.bancolombia.domain.models.enums.EventType;
import co.com.bancolombia.kafka.loggin.infraestructure.kafkaproducer.LogginAdapter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Component
public class KafkaLogEventSender {
    private final LogginAdapter kafkaProducer;

    public KafkaLogEventSender(LogginAdapter kafkaProducer) {
        this.kafkaProducer = kafkaProducer;

    }
    public Mono<Void> loggingHttpAdapter(EventType eventType, String messageId, String consumerid, String path, Object body, Map<String, String> headers, String httpMethod, Integer status) {
        return kafkaProducer.loggingAdapterEvent(eventType,
                body,
                headers,
                httpMethod,
                messageId,
                consumerid,
                path,
                Instant.now(),
                "",
                status);
    }

    public Mono<Void> loggingAdapter(EventType eventType, String messageId,String consumerid,String path, Object body) {
        return kafkaProducer.loggingAdapterEvent(eventType,
                body,
                null,
                null,
                messageId,
                consumerid,
                path,
                Instant.now(),
                "",
                null);
    }

    public Mono<Void> loggingHttpErrorAdapter(String messageId,String consumerid, String path, Object body, Map<String, String> headers, String httpMethod, Integer status, Exception error) {
        return kafkaProducer.loggingAdapterErrorEvent(
                body,
                headers,
                httpMethod,
                messageId,
                consumerid,
                path,
                Instant.now(),
                "",
                status,
                error);
    }

    public Mono<Void> loggingErrorAdapter(String messageId,String consumerid, Object body,Map<String, String> headers, Exception error) {
        return kafkaProducer.loggingAdapterErrorEvent(
                body,
                headers,
                null,
                messageId,
                consumerid,
                null,
                Instant.now(),
                "",
                null,
                error);
    }
}
