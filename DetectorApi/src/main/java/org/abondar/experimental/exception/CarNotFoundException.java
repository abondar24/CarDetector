package org.abondar.experimental.exception;

public class CarNotFoundException extends RuntimeException{

    public CarNotFoundException(){
        super("Car model not found");
    }
}
