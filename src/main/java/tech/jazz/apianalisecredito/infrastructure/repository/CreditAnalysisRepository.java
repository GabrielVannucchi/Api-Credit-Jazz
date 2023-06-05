package tech.jazz.apianalisecredito.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;

public interface CreditAnalysisRepository extends JpaRepository<CreditAnalysisEntity, UUID> {
    List<CreditAnalysisEntity> findByClientId(UUID clientId);

    Optional<CreditAnalysisEntity> findFirstById(UUID id);


}
