package tech.jazz.apianalisecredito.presentation.dto.request;

import lombok.Builder;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.RequestClientIdOutOfFormatException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.RequestMonthlyIncomeInvalidException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.RequestRequestAmountInvalidException;

import java.math.BigDecimal;

public record CreditAnalysisRequest (
    String clientId,
    BigDecimal monthlyIncome,
    BigDecimal requestedAmount
){
    @Builder
    public CreditAnalysisRequest(String clientId,
                                 BigDecimal monthlyIncome,
                                 BigDecimal requestedAmount) {
        if (!clientId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new RequestClientIdOutOfFormatException();
        }

        if (monthlyIncome.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new RequestMonthlyIncomeInvalidException();
        }

        if (requestedAmount.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new RequestRequestAmountInvalidException();
        }
        this.clientId = clientId;
        this.monthlyIncome = monthlyIncome;
        this.requestedAmount = requestedAmount;

    }
}
