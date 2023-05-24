package tech.jazz.apianalisecredito.infrastructure.mapper;

import org.mapstruct.Mapper;
import tech.jazz.apianalisecredito.infrastructure.model.CreditAnalysis;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;


@Mapper(componentModel = "spring")
public interface CreditAnalysisMapper {

    CreditAnalysisEntity from(CreditAnalysis creditAnalysis);

    ClientAnalysisResponse from(CreditAnalysisEntity creditAnalysisEntity);

}
