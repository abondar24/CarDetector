package org.abondar.experimental.model;

public enum Messages {

    CAR_NOT_FOUND("car model not detected");

    private String msg;

    Messages(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
