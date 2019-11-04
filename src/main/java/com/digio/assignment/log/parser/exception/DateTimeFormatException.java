package com.digio.assignment.log.parser.exception;

public class DateTimeFormatException extends RuntimeException {

    public DateTimeFormatException(){
        super();
    }

    public DateTimeFormatException(final String message, final Throwable cause){
        super(message, cause);
    }

    public DateTimeFormatException(final String message){
        super(message);
    }

    public DateTimeFormatException(final Throwable cause){
        super(cause);
    }
}
