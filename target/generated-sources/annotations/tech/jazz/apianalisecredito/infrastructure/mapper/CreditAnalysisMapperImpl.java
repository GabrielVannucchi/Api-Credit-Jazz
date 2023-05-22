package tech.jazz.apianalisecredito.infrastructure.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tech.jazz.apianalisecredito.infrastructure.model.CreditAnalysis;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-22T11:06:32-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class CreditAnalysisMapperImpl implements CreditAnalysisMapper {

    @Override
    public CreditAnalysisEntity from(CreditAnalysis creditAnalysis) {
        if ( creditAnalysis == null ) {
            return null;
        }

        CreditAnalysisEntity.CreditAnalysisEntityBuilder creditAnalysisEntity = CreditAnalysisEntity.builder();

        creditAnalysisEntity.approved( creditAnalysis.approved() );
        creditAnalysisEntity.approvedLimit( creditAnalysis.approvedLimit() );
        creditAnalysisEntity.requestedAmount( creditAnalysis.requestedAmount() );
        creditAnalysisEntity.withdraw( creditAnalysis.withdraw() );
        creditAnalysisEntity.annualInterest( creditAnalysis.annualInterest() );
        if ( creditAnalysis.clientId() != null ) {
            creditAnalysisEntity.clientId( creditAnalysis.clientId().toString() );
        }
        creditAnalysisEntity.date( creditAnalysis.date() );

        return creditAnalysisEntity.build();
    }

    @Override
    public ClientAnalysisResponse from(CreditAnalysisEntity creditAnalysisEntity) {
        if ( creditAnalysisEntity == null ) {
            return null;
        }

        UUID id = null;
        Boolean approved = null;
        BigDecimal approvedLimit = null;
        BigDecimal requestedAmount = null;
        BigDecimal withdraw = null;
        Integer annualInterest = null;
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

        ClientAnalysisResponse clientAnalysisResponse = new ClientAnalysisResponse( id, approved, approvedLimit, requestedAmount, withdraw, annualInterest, clientId, date );

        return clientAnalysisResponse;
    }
}
