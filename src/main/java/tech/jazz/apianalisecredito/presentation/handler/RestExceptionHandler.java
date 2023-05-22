package tech.jazz.apianalisecredito.presentation.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.*;

import java.net.URI;

@RestControllerAdvice
public class RestExceptionHandler {
    private ProblemDetail problemDetailBuilder(HttpStatus status, String title, String message, Exception e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/" + status.value()));
        problemDetail.setTitle(title);
        problemDetail.setDetail(message);
        return problemDetail;
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ProblemDetail> clientNotFoundExceptionHandler(ClientNotFoundException e){
        ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                "Client not found in ClientApi", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(ClientParamOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> clientParamOutOfFormatExceptionHandler(ClientParamOutOfFormatException e){
        ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                "Parameter out of pattern. Insert a CPF or a UUID of pattern XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(UuidOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> uuidOutOfFormatExceptionHandler(UuidOutOfFormatException e){
        ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                "Id out of pattern. Insert correct UUID of pattern XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(RequestClientIdOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> requestClientIdOutOfFormatExceptionHandler(RequestClientIdOutOfFormatException e){
        ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                "Id out of pattern. Insert correct UUID of pattern XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
    @ExceptionHandler(RequestMonthlyIncomeInvalidException.class)
    public ResponseEntity<ProblemDetail> requestMonthlyIncomeInvalidExceptionHandler(RequestMonthlyIncomeInvalidException e){
        ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                "Insert value greater than 0 for monthly income", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
    @ExceptionHandler(RequestRequestAmountInvalidException.class)
    public ResponseEntity<ProblemDetail> requestRequestAmountInvalidExceptionHandler(RequestRequestAmountInvalidException e){
        ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_GATEWAY, e.getClass().getSimpleName(),
                "Insert value greater than 0 for requested amount", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
    @ExceptionHandler(CreditAnalysisNotFoundException.class)
    public ResponseEntity<ProblemDetail> creditAnalysisNotFoundExceptionHandler(CreditAnalysisNotFoundException e){
        ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                "Credit analysis not found with this id", e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
}
