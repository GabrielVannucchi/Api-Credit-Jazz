package tech.jazz.apianalisecredito.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface AllAnalysisMapper {
    AllAnalysisResponse from(CreditAnalysisEntity creditAnalysisEntity);
}
