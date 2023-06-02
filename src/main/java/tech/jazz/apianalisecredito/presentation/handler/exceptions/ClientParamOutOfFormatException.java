package tech.jazz.apianalisecredito.presentation.handler.exceptions;

public class ClientParamOutOfFormatException extends RuntimeException {
    public ClientParamOutOfFormatException(String message) {
        super(message);
    }
}
