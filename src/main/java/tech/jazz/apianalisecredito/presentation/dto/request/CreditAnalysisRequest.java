package tech.jazz.apianalisecredito.presentation.dto.request;

import lombok.Builder;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientIdOutOfFormatException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.MonthlyIncomeInvalidException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.RequestedAmountInvalidException;

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
            throw new ClientIdOutOfFormatException();
        }

        if (monthlyIncome.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new MonthlyIncomeInvalidException();
        }

        if (requestedAmount.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new RequestedAmountInvalidException();
        }
        this.clientId = clientId;
        this.monthlyIncome = monthlyIncome;
        this.requestedAmount = requestedAmount;

    }
}
