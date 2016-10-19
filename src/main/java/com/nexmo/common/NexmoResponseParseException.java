package com.nexmo.common;


public class NexmoResponseParseException extends RuntimeException {
    public NexmoResponseParseException(String message) {
        this(message, null);
    }

    public NexmoResponseParseException(String message, Throwable t) {
        super(message, t);
    }
}
