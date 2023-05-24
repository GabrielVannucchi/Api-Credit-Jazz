package tech.jazz.apianalisecredito.presentation.handler;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientIdOutOfFormatException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientNotFoundException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientParamOutOfFormatException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.CreditAnalysisNotFoundException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.MonthlyIncomeInvalidException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.RequestedAmountInvalidException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.UuidOutOfFormatException;

@RestControllerAdvice
public class RestExceptionHandler {
    private ProblemDetail problemDetailBuilder(HttpStatus status, String title, String message, Exception e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/" + status.value()));
        problemDetail.setTitle(title);
        problemDetail.setDetail(message);
        return problemDetail;
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ProblemDetail> clientNotFoundExceptionHandler(ClientNotFoundException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                "Client not found in ClientApi", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(ClientParamOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> clientParamOutOfFormatExceptionHandler(ClientParamOutOfFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                "Parameter out of pattern. Insert a CPF or a UUID of pattern XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(UuidOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> uuidOutOfFormatExceptionHandler(UuidOutOfFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                "Id out of pattern. Insert correct UUID of pattern XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(ClientIdOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> requestClientIdOutOfFormatExceptionHandler(ClientIdOutOfFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                "Id out of pattern. Insert correct UUID of pattern XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(MonthlyIncomeInvalidException.class)
    public ResponseEntity<ProblemDetail> requestMonthlyIncomeInvalidExceptionHandler(MonthlyIncomeInvalidException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                "Insert value greater than 0 and a scale greater than 2 for monthly income", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(RequestedAmountInvalidException.class)
    public ResponseEntity<ProblemDetail> requestRequestAmountInvalidExceptionHandler(RequestedAmountInvalidException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_GATEWAY, e.getClass().getSimpleName(),
                "Insert value greater than 0 and a scale greater than 2 for requested amount", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CreditAnalysisNotFoundException.class)
    public ResponseEntity<ProblemDetail> creditAnalysisNotFoundExceptionHandler(CreditAnalysisNotFoundException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                "Credit analysis not found with this id", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
}
