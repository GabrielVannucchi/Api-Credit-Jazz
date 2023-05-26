package tech.jazz.apianalisecredito.presentation.handler.exceptions;

public class CreditAnalysisNotFoundException extends RuntimeException {
    public CreditAnalysisNotFoundException(String message) {
        super(message);
    }
}
