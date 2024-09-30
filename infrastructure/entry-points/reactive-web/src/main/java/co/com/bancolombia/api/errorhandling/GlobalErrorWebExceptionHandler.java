package co.com.bancolombia.api.errorhandling;
import co.com.bancolombia.api.commons.Meta;
import co.com.bancolombia.model.validation.exceptions.BusinessException;
import co.com.bancolombia.model.validation.exceptions.SecurityException;
import co.com.bancolombia.model.validation.exceptions.TechnicalException;
import co.com.bancolombia.model.validation.exceptions.message.BusinessErrorMessage;
import co.com.bancolombia.model.validation.exceptions.message.SecurityErrorMessage;
import co.com.bancolombia.model.validation.exceptions.message.TechnicalErrorMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.*;


@Component
@Slf4j
@Order(-3)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    //private final KafkaLogEventSender loggingAdapter;

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties resources,
                                          ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer
                                          /*,KafkaLogEventSender loggingAdapter*/) {
        super(errorAttributes, resources.getResources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        //this.loggingAdapter = loggingAdapter;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorMessage);
    }

    @SneakyThrows
    private Mono<ServerResponse> renderErrorMessage(ServerRequest serverRequest){

        return Mono.error(getError(serverRequest))
                .onErrorResume(SecurityException.class, error -> {
                    log.warn("SecurityException", error);
                    List<Error> errorList = new ArrayList<>();
                    int status = Optional.ofNullable(error.getSecurityErrorMessage())
                            .map(SecurityErrorMessage::getStatus).orElse(403) ;

                    errorList.add(Error.builder()
                            .code(Optional.ofNullable(error.getSecurityErrorMessage())
                                    .map(SecurityErrorMessage::getCode)
                                    .orElse(""))
                            .detail(Optional.ofNullable(error.getSecurityErrorMessage())
                                    .map(SecurityErrorMessage::getMessage)
                                    .orElse(error.getMessage()))
                            .build());

                    var body = buildResponseBody(
                            Optional.ofNullable(error.getSecurityErrorMessage())
                                    .map(SecurityErrorMessage::getTitle)
                                    .orElse(""),
                            errorList,
                            status,
                            Meta.generateMeta(serverRequest));

                   /*loggingAdapter.loggingHttpErrorAdapter(serverRequest.headers().firstHeader("message-id"),serverRequest.headers().firstHeader("consumer-id"), serverRequest.path(),
                            body,serverRequest.headers().asHttpHeaders().toSingleValueMap(),"POST",status,error);*/

                    return ServerResponse
                            .status(status)
                            .bodyValue(body);

                }).onErrorResume(TechnicalException.class, error -> {
                    log.warn("TechnicalException", error);
                    List<Error> errorList = new ArrayList<>();
                    int status = Optional.ofNullable(error.getTechnicalErrorMessage())
                            .map(TechnicalErrorMessage::getStatus).orElse(500) ;

                    errorList.add(Error.builder()
                            .code(Optional.ofNullable(error.getTechnicalErrorMessage()).map(TechnicalErrorMessage::getCode).orElse(""))
                            .detail(Optional.ofNullable (error.getTechnicalErrorMessage()).map(TechnicalErrorMessage::getMessage).orElse(error.getMessage()))
                            .build());

                    var body = buildResponseBody(
                            Optional.ofNullable(error.getTechnicalErrorMessage())
                                    .map(TechnicalErrorMessage::getTitle)
                                    .orElse("Technical intern error"),
                            errorList,
                            status,
                            Meta.generateMeta(serverRequest)
                    );

                   /* loggingAdapter.loggingHttpErrorAdapter(serverRequest.headers().firstHeader("message-id"),serverRequest.headers().firstHeader("consumer-id"), serverRequest.path(),
                            body,serverRequest.headers().asHttpHeaders().toSingleValueMap(),"POST",status,error);¨*/


                    return ServerResponse
                            .status(status)
                            .bodyValue(body);

                }).onErrorResume(BusinessException.class, error -> {
                    log.warn("BussinesException", error);
                    List<Error> errorList = new ArrayList<>();
                    int status = Optional.ofNullable(error.getBussinesErrorMessage())
                                    .map(BusinessErrorMessage::getStatus)
                            .orElse(error.getStatus());

                    errorList.add(Error.builder()
                            .code(Optional.ofNullable(error.getBussinesErrorMessage())
                                            .map(BusinessErrorMessage::getCode)
                                    .orElse(error.getCode()))
                            .detail(Optional.ofNullable(error.getBussinesErrorMessage())
                                            .map(BusinessErrorMessage::getMessage)
                                            .orElse(error.getMessageError()))
                            .build());

                    var body = buildResponseBody(
                            Optional.ofNullable(error.getBussinesErrorMessage())
                                            .map(BusinessErrorMessage::getTitle)
                                    .orElse(""),
                            errorList,
                            status,
                            Meta.generateMeta(serverRequest)
                    );

                   /* loggingAdapter.loggingHttpErrorAdapter(serverRequest.headers().firstHeader("message-id"),serverRequest.headers().firstHeader("consumer-id"), serverRequest.path(),
                            body,serverRequest.headers().asHttpHeaders().toSingleValueMap(),"POST",status,error);*/

                    return ServerResponse
                            .status(status)
                            .bodyValue(body);

                }).cast(ServerResponse.class);

    }


    private Map<String, Object> buildResponseBody(String title, List<Error> errorList, int status, Meta meta) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("meta", meta);
        body.put("title", title);
        body.put("error", errorList);
        body.put("status", status);

        return body;
    }


}
