package co.com.bancolombia.api.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static co.com.bancolombia.api.commons.Headers.CONSUMER_ID;
import static co.com.bancolombia.api.commons.Headers.MESSAGE_ID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta {

    @JsonProperty("_messageId")
    private String messageId;
    @JsonProperty("_requestDateTime")
    private String requestDateTime;
    @JsonProperty("_applicationId")
    private String application;

    // Cambiar metodo
    public static Mono<ObjectNode> mergeWithMeta(Meta meta, String jsonData){
        return Mono.fromCallable(()->{
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = (ObjectNode) objectMapper.readTree(jsonData);
            ObjectNode metaNode = objectMapper.valueToTree(meta);

            ObjectNode newRoot = objectMapper.createObjectNode();
            newRoot.set("meta",metaNode);
            newRoot.set("data",rootNode.get("data"));
            return newRoot;
        });
    }

    public static Meta generateMeta(ServerRequest serverRequest) {
        return Meta.builder()
                .messageId(serverRequest.headers().firstHeader(MESSAGE_ID))
                .requestDateTime(ZonedDateTime.now(ZoneId.of("America/Bogota"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                                .withZone(ZoneId.systemDefault())))
                .application(serverRequest.headers().firstHeader(CONSUMER_ID))
                .build();
    }




}
