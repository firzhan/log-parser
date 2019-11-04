package com.digio.assignment.log.parser.exception;

public class LogFileNotFoundException extends RuntimeException {

    public LogFileNotFoundException(){
        super();
    }

    public LogFileNotFoundException(final String message, final Throwable cause){
        super(message, cause);
    }

    public LogFileNotFoundException(final String message){
        super(message);
    }

    public LogFileNotFoundException(final Throwable cause){
        super(cause);
    }
}
