package tech.jazz.apianalisecredito.infrastructure.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    static final BigDecimal MAX_INCOME = BigDecimal.valueOf(50000.00);
    static float ANNUAL_INTEREST = 15.0f;
    static final float HIGH_REQUEST_PERCENT = 15.0f;
    static final float LOW_REQUEST_PERCENT = 30.0f;
    static final float WITHDRAW_PERCENT = 10.0f;

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

    public static CreditAnalysis newCreditAnalysis(BigDecimal monthlyIncome, BigDecimal requestedAmount, UUID clientId) {
        BigDecimal incomeValue = monthlyIncome;
        BigDecimal withdraw = new BigDecimal(0);
        BigDecimal approvedLimit = new BigDecimal(0);
        final boolean approved;

        if (requestedAmount.compareTo(monthlyIncome) > 0) {
            approved = false;
        } else {
            approved = true;
            if (monthlyIncome.compareTo(MAX_INCOME) > 0) {
                incomeValue = MAX_INCOME;
            }
            if (requestedAmount.compareTo(monthlyIncome.multiply(BigDecimal.valueOf(0.5))) == 1) {
                approvedLimit = incomeValue.multiply(BigDecimal.valueOf(HIGH_REQUEST_PERCENT / 100));
            } else {
                approvedLimit = incomeValue.multiply(BigDecimal.valueOf(LOW_REQUEST_PERCENT / 100));
            }
            approvedLimit = approvedLimit.setScale(2, RoundingMode.DOWN);
            withdraw = approvedLimit.multiply(BigDecimal.valueOf(WITHDRAW_PERCENT / 100)).setScale(2, RoundingMode.DOWN);
        }


        final CreditAnalysis creditAnalysis = CreditAnalysis.builder()
                .approved(approved)
                .approvedLimit(approvedLimit)
                .requestedAmount(requestedAmount)
                .withdraw(withdraw)
                .annualInterest(ANNUAL_INTEREST)
                .clientId(clientId)
                .date(LocalDateTime.now())
                .build();

        return creditAnalysis;
    }
}
