package tech.jazz.apianalisecredito.presentation.handler.exceptions;

public class RequestedAmountInvalidException extends RuntimeException {
    public RequestedAmountInvalidException(String message) {
        super(message);
    }
}
