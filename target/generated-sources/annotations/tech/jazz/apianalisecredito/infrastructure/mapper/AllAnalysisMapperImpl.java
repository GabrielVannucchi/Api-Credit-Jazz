package tech.jazz.apianalisecredito.infrastructure.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-22T14:57:08-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class AllAnalysisMapperImpl implements AllAnalysisMapper {

    @Override
    public AllAnalysisResponse from(CreditAnalysisEntity creditAnalysisEntity) {
        if ( creditAnalysisEntity == null ) {
            return null;
        }

        UUID id = null;
        Boolean approved = null;
        BigDecimal approvedLimit = null;
        BigDecimal requestedAmount = null;
        BigDecimal withdraw = null;
        Float annualInterest = null;
        String clientId = null;
        LocalDateTime date = null;

        id = creditAnalysisEntity.getId();
        approved = creditAnalysisEntity.getApproved();
        approvedLimit = creditAnalysisEntity.getApprovedLimit();
        requestedAmount = creditAnalysisEntity.getRequestedAmount();
        withdraw = creditAnalysisEntity.getWithdraw();
        annualInterest = creditAnalysisEntity.getAnnualInterest();
        clientId = creditAnalysisEntity.getClientId();
        date = creditAnalysisEntity.getDate();

        AllAnalysisResponse allAnalysisResponse = new AllAnalysisResponse( id, approved, approvedLimit, requestedAmount, withdraw, annualInterest, clientId, date );

        return allAnalysisResponse;
    }
}
