package tech.jazz.apianalisecredito.applicationservice.creditservice;

import feign.FeignException;
import feign.RetryableException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apianalisecredito.infrastructure.domain.CreditAnalysis;
import tech.jazz.apianalisecredito.infrastructure.mapper.AllAnalysisMapper;
import tech.jazz.apianalisecredito.infrastructure.mapper.CreditAnalysisMapper;
import tech.jazz.apianalisecredito.infrastructure.repository.CreditAnalysisRepository;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.request.CreditAnalysisRequest;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientApiUnavailableException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientNotFoundException;


@Service
@RequiredArgsConstructor
public class CreateCreditAnalysisService {
    private final CreditAnalysisRepository repository;
    private final CreditAnalysisMapper creditAnalysisMapper;
    private final AllAnalysisMapper allAnalysisMapper;
    private final ClientApi clientApi;
    private final String uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final String cpfregex = "(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})";
    private final String clientNotFoundMessage = "Client not found in ClientApi";
    private final String clientApiUnavailableMessage = "Client API unavailable";

    public ClientAnalysisResponse createAnalysis(CreditAnalysisRequest request) {
        try {
            clientApi.getClientById(request.clientId());
        } catch (RetryableException e) {
            throw new ClientApiUnavailableException(clientApiUnavailableMessage);
        } catch (FeignException e) {
            throw new ClientNotFoundException(clientNotFoundMessage);
        }

        final CreditAnalysis creditAnalysis = CreditAnalysis.newCreditAnalysis(
                request.monthlyIncome(), request.requestedAmount(), UUID.fromString(request.clientId()));

        final CreditAnalysisEntity entity = creditAnalysisMapper.from(creditAnalysis);
        return creditAnalysisMapper.from(repository.save(entity));
    }
}
