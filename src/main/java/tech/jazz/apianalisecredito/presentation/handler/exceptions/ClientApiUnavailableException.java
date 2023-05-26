package tech.jazz.apianalisecredito.presentation.handler.exceptions;

public class ClientApiUnavailableException extends RuntimeException {
    public ClientApiUnavailableException(String message) {
        super(message);
    }
}
