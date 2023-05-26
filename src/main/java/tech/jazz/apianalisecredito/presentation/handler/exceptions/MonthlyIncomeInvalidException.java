package tech.jazz.apianalisecredito.presentation.handler.exceptions;

public class MonthlyIncomeInvalidException extends RuntimeException {
    public MonthlyIncomeInvalidException(String message) {
        super(message);
    }
}
