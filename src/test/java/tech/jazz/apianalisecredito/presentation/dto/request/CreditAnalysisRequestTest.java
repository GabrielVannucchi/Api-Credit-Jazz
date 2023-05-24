package tech.jazz.apianalisecredito.presentation.dto.request;

import org.junit.jupiter.api.Test;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientIdOutOfFormatException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.MonthlyIncomeInvalidException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.RequestedAmountInvalidException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CreditAnalysisRequestTest {
    @Test
    void should_throw_ClientIdOutOfFormatException_when_UUID_is_wrong(){
        assertThrows(ClientIdOutOfFormatException.class,() -> CreditAnalysisRequest.builder()
                .clientId("incorrect UUID")
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(50000))
                .build());
    }

    @Test
    void should_throw_MonthlyIncomeInvalidException_when_monthlyIncome_is_negative(){
        assertThrows(MonthlyIncomeInvalidException.class,() -> CreditAnalysisRequest.builder()
                .clientId("690cfa4d-2228-4343-85db-82e96e122da9")
                .monthlyIncome(new BigDecimal(-20000))
                .requestedAmount(new BigDecimal(50000))
                .build());
    }

    @Test
    void should_throw_RequestedAmountInvalidException_when_requestedAmount_is_negative(){
        assertThrows(RequestedAmountInvalidException.class,() -> CreditAnalysisRequest.builder()
                .clientId("690cfa4d-2228-4343-85db-82e96e122da9")
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(-50000))
                .build());
    }

    @Test
    void should_throw_MonthlyIncomeInvalidException_when_requestedAmount_have_incorrect_scale(){
        assertThrows(MonthlyIncomeInvalidException.class,() -> CreditAnalysisRequest.builder()
                .clientId("690cfa4d-2228-4343-85db-82e96e122da9")
                .monthlyIncome(new BigDecimal(20000.0001))
                .requestedAmount(new BigDecimal(5000))
                .build());
    }

    @Test
    void should_throw_RequestedAmountInvalidException_when_requestedAmount_have_incorrect_scale(){
        assertThrows(RequestedAmountInvalidException.class,() -> CreditAnalysisRequest.builder()
                .clientId("690cfa4d-2228-4343-85db-82e96e122da9")
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(5000.005))
                .build());
    }
}