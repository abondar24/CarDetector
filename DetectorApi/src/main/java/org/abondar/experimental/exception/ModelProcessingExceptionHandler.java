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
@Requires(classes = {ModelProcessingException.class, ExceptionHandler.class})
public class ModelProcessingExceptionHandler implements ExceptionHandler<ModelProcessingException, HttpResponse<?>>{
    @Override
    public HttpResponse<?> handle(HttpRequest request, ModelProcessingException exception) {
        return HttpResponse.status(HttpStatus.NOT_IMPLEMENTED,exception.getMessage());
    }
}
