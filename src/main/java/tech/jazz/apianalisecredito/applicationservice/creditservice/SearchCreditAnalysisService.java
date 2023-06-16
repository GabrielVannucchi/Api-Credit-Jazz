package tech.jazz.apianalisecredito.applicationservice.creditservice;

import feign.FeignException;
import feign.RetryableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.jazz.apianalisecredito.infrastructure.mapper.AllAnalysisMapper;
import tech.jazz.apianalisecredito.infrastructure.mapper.CreditAnalysisMapper;
import tech.jazz.apianalisecredito.infrastructure.repository.CreditAnalysisRepository;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientApiUnavailableException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientNotFoundException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.CpfOutOfFormatException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.CreditAnalysisNotFoundException;

@Service
@RequiredArgsConstructor
public class SearchCreditAnalysisService {
    private final CreditAnalysisRepository repository;
    private final CreditAnalysisMapper creditAnalysisMapper;
    private final AllAnalysisMapper allAnalysisMapper;
    private final ClientApi clientApi;
    private static final String CPF_REGEX = "(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})";
    private final String clientNotFoundMessage = "Client not found in ClientApi";
    private final String clientApiUnavailableMessage = "Client API unavailable";

    public List<AllAnalysisResponse> listAllCreditAnalysis() {
        final List<CreditAnalysisEntity> listEntity = repository.findAll();
        final List<AllAnalysisResponse> listResponse = new ArrayList<>();
        for (CreditAnalysisEntity entity :
                listEntity) {
            listResponse.add(allAnalysisMapper.from(entity));
        }

        return listResponse;
    }

    public List<ClientAnalysisResponse> listAnalysisByClient(UUID id, String cpf) {
        if (id != null) {
            try {
                clientApi.getClientId(id);
            } catch (RetryableException e) {
                throw new ClientApiUnavailableException(clientApiUnavailableMessage);
            } catch (FeignException e) {
                throw new ClientNotFoundException(clientNotFoundMessage);
            }
        }
        if (cpf != null) {
            if (Pattern.matches(CPF_REGEX, cpf)) {
                try {
                    id = UUID.fromString(clientApi.getClientCpf(cpf).get(0).id());
                } catch (RetryableException e) {
                    throw new ClientApiUnavailableException(clientApiUnavailableMessage);
                } catch (FeignException e) {
                    throw new ClientNotFoundException(clientNotFoundMessage);
                }
            } else {
                throw new CpfOutOfFormatException("Given Cpf out of format");
            }
        }
        final List<CreditAnalysisEntity> listEntity = repository.findByClientId(id);
        final List<ClientAnalysisResponse> listResponse = new ArrayList<>();
        for (CreditAnalysisEntity entity :
                listEntity) {
            listResponse.add(creditAnalysisMapper.from(entity));
        }
        return listResponse;
    }

    public ClientAnalysisResponse findAnalysisById(UUID id) {
        final Optional<CreditAnalysisEntity> optionalEntity = repository.findFirstById(id);

        final CreditAnalysisEntity entity = optionalEntity.orElseThrow(
                () -> new CreditAnalysisNotFoundException(String.format("Credit analysis not found with id %s", id)));

        return creditAnalysisMapper.from(entity);
    }

}
