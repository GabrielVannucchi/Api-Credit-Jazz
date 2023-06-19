package tech.jazz.apianalisecredito.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import tech.jazz.apianalisecredito.infrastructure.domain.CreditAnalysis;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CreditAnalysisMapper {

    CreditAnalysisEntity from(CreditAnalysis creditAnalysis);

    ClientAnalysisResponse from(CreditAnalysisEntity creditAnalysisEntity);

}
