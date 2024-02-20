package org.abondar.experimental.exception;

public class ModelNotReadyException extends RuntimeException{
    public ModelNotReadyException() {
        super("Model or annotations are not downloaded");
    }
}
