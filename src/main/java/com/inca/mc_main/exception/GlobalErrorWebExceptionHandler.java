package com.inca.mc_main.exception;
import com.inca.mc_main.exception.response.ErrorResponse;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          WebProperties.Resources resources,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
        this.setMessageReaders(codecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);

        boolean status = false;
        int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = "Ocurrió un error inesperado.";
        Object data = null;

        if (error instanceof ApiException apiEx) {
            status = apiEx.getStatus();
            code = apiEx.getCode();
            message = apiEx.getMessage();
            data = apiEx.getData();
        } else if (error instanceof WebExchangeBindException bindException) {
            code = HttpStatus.BAD_REQUEST.value();
            message = "Errores de validación";
            data = bindException.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .toList();
        } else if (error instanceof IllegalArgumentException ex) {
            code = HttpStatus.BAD_REQUEST.value();
            message = ex.getMessage();
        }else if (error instanceof org.springframework.web.server.ServerWebInputException ex) {
            code = HttpStatus.BAD_REQUEST.value();
            message = "Parámetro inválido en la solicitud: " + ex.getReason();
        }

        ErrorResponse<Object> response = ErrorResponse.<Object>builder()
                .status(status)
                .code(code)
                .message(message)
                .data(data)
                .build();

        return ServerResponse
                .status(code)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
