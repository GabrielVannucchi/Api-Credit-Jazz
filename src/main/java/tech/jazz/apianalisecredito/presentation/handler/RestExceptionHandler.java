package tech.jazz.apianalisecredito.presentation.handler;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientApiUnavailableException;
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
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(ClientApiUnavailableException.class)
    public ResponseEntity<ProblemDetail> clientApiUnavailableExceptionHandler(ClientApiUnavailableException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.SERVICE_UNAVAILABLE, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(ClientParamOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> clientParamOutOfFormatExceptionHandler(ClientParamOutOfFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(UuidOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> uuidOutOfFormatExceptionHandler(UuidOutOfFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(ClientIdOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> clientIdOutOfFormatExceptionHandler(ClientIdOutOfFormatException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(MonthlyIncomeInvalidException.class)
    public ResponseEntity<ProblemDetail> monthlyIncomeInvalidExceptionHandler(MonthlyIncomeInvalidException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(RequestedAmountInvalidException.class)
    public ResponseEntity<ProblemDetail> requestAmountInvalidExceptionHandler(RequestedAmountInvalidException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CreditAnalysisNotFoundException.class)
    public ResponseEntity<ProblemDetail> creditAnalysisNotFoundExceptionHandler(CreditAnalysisNotFoundException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
    /*
    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<ProblemDetail> callNotPermittedExceptionHandler(CallNotPermittedException e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.SERVICE_UNAVAILABLE, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
    */
}
