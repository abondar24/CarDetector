package org.abondar.experimental.exception;

public class ModelProcessingException extends RuntimeException{

    public ModelProcessingException(){
        super("Error processing uploaded image");
    }
}
