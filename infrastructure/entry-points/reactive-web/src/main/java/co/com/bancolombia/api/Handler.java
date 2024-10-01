package co.com.bancolombia.api;

import co.com.bancolombia.domain.models.enums.EventType;
import co.com.bancolombia.iastpatternconfig.TransformService;
import co.com.bancolombia.iastpatternconfig.config.PatternModel;
import co.com.bancolombia.loggingandmonitoringservice.loggingadapter.KafkaLogEventSender;
import co.com.bancolombia.model.validation.exceptions.BusinessDetailException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static co.com.bancolombia.iastpatternconfig.TransformService.generateRequest;
import static co.com.bancolombia.iastpatternconfig.TransformService.generateResponse;


@Component
@RequiredArgsConstructor
public class Handler {

    private final Map<String, PatternModel> configModels;
    public static final String HEADERS = "headers";
    public static final String OPERATION = "operation";
    private final ObjectMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private final KafkaLogEventSender loggingAdapter;

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {

        var headers = serverRequest.headers().asHttpHeaders().toSingleValueMap();
        var operation = serverRequest.pathVariable(OPERATION);
        // usecase.logic();
        return serverRequest.bodyToMono(ObjectNode.class)
                .doOnNext(request ->
                        loggingAdapter.loggingHttpAdapter(
                                EventType.REQUEST,
                                serverRequest.headers().firstHeader("message-id"),
                                serverRequest.headers().firstHeader("id-consumer"),
                                serverRequest.path(),
                                request,
                                serverRequest.headers().asHttpHeaders().toSingleValueMap(),
                                serverRequest.method().name(),
                                null))
                //.switchIfEmpty(Mono.error(() -> new BusinessDetailException(VALIDATION_DATA_ERROR, "Invalid request")))
                .map(rootNode -> {


                    var headersNode = mapper.valueToTree(headers);
                    rootNode.set(HEADERS, headersNode);

                    return generateRequest(rootNode,configModels.get(operation).requestModel(), null);
                        })

                .flatMap(s -> ServerResponse.ok().bodyValue(s)
                        .doOnNext(rs->
                                loggingAdapter.loggingHttpAdapter(
                                        EventType.RESPONSE,
                                        serverRequest.headers().firstHeader("message-id"),
                                        serverRequest.headers().firstHeader("consumer-id"),
                                        serverRequest.path(),
                                        s,
                                        serverRequest.headers().asHttpHeaders().toSingleValueMap(),
                                        serverRequest.method().name(),
                                        rs.statusCode().value())));
    }

    public Mono<ServerResponse> listenPOSTUseCase2(ServerRequest serverRequest) {

        var headers = serverRequest.headers().asHttpHeaders().toSingleValueMap();
        var operation = serverRequest.pathVariable(OPERATION);
        // usecase.logic();
        return serverRequest.bodyToMono(JsonNode.class)
                .doOnNext(request ->
                        loggingAdapter.loggingHttpAdapter(
                                EventType.REQUEST,
                                serverRequest.headers().firstHeader("message-id"),
                                serverRequest.headers().firstHeader("id-consumer"),
                                serverRequest.path(),
                                request,
                                serverRequest.headers().asHttpHeaders().toSingleValueMap(),
                                serverRequest.method().name(),
                                null))
                //.switchIfEmpty(Mono.error(() -> new BusinessDetailException(VALIDATION_DATA_ERROR, "Invalid request")))
                .map(node -> {
                    String trama = node.get("trama").asText();
                    log.info("Request : "+trama);
                    return generateResponse(trama,configModels.get(operation).responseModels(), null);
                })
                .flatMap(s -> ServerResponse.ok().bodyValue(s)
                        .doOnNext(rs->
                                loggingAdapter.loggingHttpAdapter(
                                        EventType.RESPONSE,
                                        serverRequest.headers().firstHeader("message-id"),
                                        serverRequest.headers().firstHeader("consumer-id"),
                                        serverRequest.path(),
                                        s,
                                        serverRequest.headers().asHttpHeaders().toSingleValueMap(),
                                        serverRequest.method().name(),
                                        rs.statusCode().value())));
    }
}
