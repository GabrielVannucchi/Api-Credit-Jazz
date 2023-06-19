package tech.jazz.apianalisecredito.applicationservice.creditservice;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apianalisecredito.infrastructure.domain.CreditAnalysis;
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
    private final ClientApi clientApi;

    public ClientAnalysisResponse createAnalysis(CreditAnalysisRequest request) {
        final String clientNotFoundMessage = "Client not found in ClientApi";
        final String clientApiUnavailableMessage = "Client API unavailable";
        try {
            clientApi.getClientId(request.clientId());
        } catch (RetryableException e) {
            throw new ClientApiUnavailableException(clientApiUnavailableMessage);
        } catch (FeignException e) {
            throw new ClientNotFoundException(clientNotFoundMessage);
        }

        final CreditAnalysis creditAnalysis = CreditAnalysis.newCreditAnalysis(
                request.monthlyIncome(), request.requestedAmount(), request.clientId());

        final CreditAnalysisEntity entity = creditAnalysisMapper.from(creditAnalysis);
        return creditAnalysisMapper.from(repository.save(entity));
    }


}
