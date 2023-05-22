package tech.jazz.apianalisecredito.infrastructure.mapper;

import org.mapstruct.Mapper;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;

@Mapper(componentModel = "spring")
public interface AllAnalysisMapper {
    AllAnalysisResponse from(CreditAnalysisEntity creditAnalysisEntity);
}
