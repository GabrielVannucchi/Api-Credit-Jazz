package tech.jazz.apianalisecredito.presentation.handler.exceptions;

public class CpfOutOfFormatException extends RuntimeException {
    public CpfOutOfFormatException(String message) {
        super(message);
    }
}
