package org.abondar.experimental.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;

import java.io.IOException;

@Produces
@Singleton
@Requires(classes = {IOException.class, ExceptionHandler.class})
public class IoExceptionHandler  implements ExceptionHandler<IOException, HttpResponse<?>>{
    @Override
    public HttpResponse<?> handle(HttpRequest request,IOException exception) {
        return HttpResponse.badRequest().body(exception.getMessage());
    }
}



