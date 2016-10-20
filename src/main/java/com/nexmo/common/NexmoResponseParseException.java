package com.nexmo.common;


public class NexmoResponseParseException extends Exception {
    public NexmoResponseParseException(String message) {
        this(message, null);
    }

    public NexmoResponseParseException(String message, Throwable t) {
        super(message, t);
    }
}
