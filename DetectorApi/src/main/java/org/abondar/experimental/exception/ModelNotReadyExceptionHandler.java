package org.abondar.experimental.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {ModelNotReadyException.class, ExceptionHandler.class})
public class ModelNotReadyExceptionHandler  implements ExceptionHandler<ModelNotReadyException, HttpResponse<?>>{
@Override
public HttpResponse<?> handle(HttpRequest request, ModelNotReadyException exception) {
        return HttpResponse.status(HttpStatus.BAD_GATEWAY,exception.getMessage());
        }
}


