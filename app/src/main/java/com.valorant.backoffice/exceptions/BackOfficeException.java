package com.valorant.backoffice.exceptions;

public class BackOfficeException extends RuntimeException {
    public BackOfficeException() {
        super();
    }

    public BackOfficeException(String message) {
        super(message);
    }

    public BackOfficeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackOfficeException(Throwable cause) {
        super(cause);
    }

    public BackOfficeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
