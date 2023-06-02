package tech.jazz.apianalisecredito.presentation.handler.exceptions;

public class ClientIdOutOfFormatException extends RuntimeException {
    public ClientIdOutOfFormatException(String message) {
        super(message);
    }
}
