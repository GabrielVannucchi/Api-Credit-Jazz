package tech.jazz.apianalisecredito.presentation.dto.request;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.MonthlyIncomeInvalidException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.RequestedAmountInvalidException;

public record CreditAnalysisRequest(
        UUID clientId,
        BigDecimal monthlyIncome,
        BigDecimal requestedAmount
) {
    @Builder
    public CreditAnalysisRequest(UUID clientId,
                                 BigDecimal monthlyIncome,
                                 BigDecimal requestedAmount) {
        final int scale = 2;
        if (monthlyIncome.compareTo(BigDecimal.valueOf(0)) < 0 || monthlyIncome.scale() > scale) {
            throw new MonthlyIncomeInvalidException("Insert value greater than 0 and a scale greater than 2 for monthly income");
        }

        if (requestedAmount.compareTo(BigDecimal.valueOf(0)) < 0 || requestedAmount.scale() > scale) {
            throw new RequestedAmountInvalidException("Insert value greater than 0 and a scale greater than 2 for requested amount");
        }
        this.clientId = clientId;
        this.monthlyIncome = monthlyIncome;
        this.requestedAmount = requestedAmount;

    }
}
