package edu.ga.master.ga.exceptions;

public class GAInconsistencyException extends Exception {

    public GAInconsistencyException() {
    }

    public GAInconsistencyException(String message) {
        super(message);
    }

    public GAInconsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public GAInconsistencyException(Throwable cause) {
        super(cause);
    }

    public GAInconsistencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
