package tech.jazz.apianalisecredito.infrastructure.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

public record CreditAnalysis(
        Boolean approved,
        BigDecimal approvedLimit,
        BigDecimal requestedAmount,
        BigDecimal withdraw,
        Float annualInterest,
        UUID clientId,
        LocalDateTime date
) {
    @Builder
    public CreditAnalysis(Boolean approved, BigDecimal approvedLimit, BigDecimal
            requestedAmount, BigDecimal withdraw, Float annualInterest, UUID clientId, LocalDateTime date) {
        this.approved = approved;
        this.approvedLimit = approvedLimit;
        this.requestedAmount = requestedAmount;
        this.withdraw = withdraw;
        this.annualInterest = annualInterest;
        this.clientId = clientId;
        this.date = date;
    }
}
