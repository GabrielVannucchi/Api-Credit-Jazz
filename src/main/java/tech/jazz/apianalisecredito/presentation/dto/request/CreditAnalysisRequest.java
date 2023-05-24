package tech.jazz.apianalisecredito.presentation.dto.request;

import java.math.BigDecimal;
import lombok.Builder;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientIdOutOfFormatException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.MonthlyIncomeInvalidException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.RequestedAmountInvalidException;

public record CreditAnalysisRequest(
        String clientId,
        BigDecimal monthlyIncome,
        BigDecimal requestedAmount
) {
    @Builder
    public CreditAnalysisRequest(String clientId,
                                 BigDecimal monthlyIncome,
                                 BigDecimal requestedAmount) {
        final int scale = 2;
        if (!clientId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new ClientIdOutOfFormatException();
        }

        if (monthlyIncome.compareTo(BigDecimal.valueOf(0)) < 0 || monthlyIncome.scale() > scale) {
            throw new MonthlyIncomeInvalidException();
        }

        if (requestedAmount.compareTo(BigDecimal.valueOf(0)) < 0 || requestedAmount.scale() > scale) {
            throw new RequestedAmountInvalidException();
        }
        this.clientId = clientId;
        this.monthlyIncome = monthlyIncome;
        this.requestedAmount = requestedAmount;

    }
}
