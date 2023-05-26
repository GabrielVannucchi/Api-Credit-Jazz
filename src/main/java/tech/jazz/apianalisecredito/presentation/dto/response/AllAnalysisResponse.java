package tech.jazz.apianalisecredito.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AllAnalysisResponse(
        UUID id,
        Boolean approved,
        BigDecimal approvedLimit,
        BigDecimal requestedAmount,
        BigDecimal withdraw,
        Float annualInterest,
        String clientId,
        LocalDateTime date
){
}
