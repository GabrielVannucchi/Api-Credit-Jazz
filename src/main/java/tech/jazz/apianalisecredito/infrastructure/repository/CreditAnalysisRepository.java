package tech.jazz.apianalisecredito.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditAnalysisRepository extends JpaRepository<CreditAnalysisEntity, UUID> {
    List<CreditAnalysisEntity> findByClientId(String clientId);

    Optional<CreditAnalysisEntity> findFirstById(UUID id);


}
