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
import tech.jazz.apianalisecredito.applicationservice.dto.ClientApiRequest;
import tech.jazz.apianalisecredito.infrastructure.mapper.AllAnalysisMapper;
import tech.jazz.apianalisecredito.infrastructure.mapper.CreditAnalysisMapper;
import tech.jazz.apianalisecredito.infrastructure.repository.CreditAnalysisRepository;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientApiUnavailableException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientNotFoundException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientParamOutOfFormatException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.CreditAnalysisNotFoundException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.UuidOutOfFormatException;

@Service
@RequiredArgsConstructor
public class SearchCreditAnalysisService {
    private final CreditAnalysisRepository repository;
    private final CreditAnalysisMapper creditAnalysisMapper;
    private final AllAnalysisMapper allAnalysisMapper;
    private final ClientApi clientApi;
    private final String uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final String cpfregex = "(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})";
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

    public List<ClientAnalysisResponse> listAnalysisByClient(String param) {

        if (Pattern.matches(cpfregex, param)) {
            try {
                final ClientApiRequest client = clientApi.getClientByCpf(param);
                param = client.id();
            } catch (RetryableException e) {
                throw new ClientApiUnavailableException(clientApiUnavailableMessage);
            } catch (FeignException e) {
                throw new ClientNotFoundException(clientNotFoundMessage);
            }
        } else if (!Pattern.matches(uuidRegex, param)) {
            final String message = "Parameter out of pattern. Insert a CPF or a UUID of pattern XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
            throw new ClientParamOutOfFormatException(message);
        } else {
            try {
                final ClientApiRequest client = clientApi.getClientById(param);
            } catch (RetryableException e) {
                throw new ClientApiUnavailableException(clientApiUnavailableMessage);
            } catch (FeignException e) {
                throw new ClientNotFoundException(clientNotFoundMessage);
            }
        }
        final List<CreditAnalysisEntity> listEntity = repository.findByClientId(param);
        final List<ClientAnalysisResponse> listResponse = new ArrayList<>();
        for (CreditAnalysisEntity entity :
                listEntity) {
            listResponse.add(creditAnalysisMapper.from(entity));
        }
        return listResponse;
    }

    public ClientAnalysisResponse findAnalysisById(String id) {
        if (!Pattern.matches(uuidRegex, id)) {
            throw new UuidOutOfFormatException("Id out of pattern. Insert correct UUID of pattern XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX");
        }
        final Optional<CreditAnalysisEntity> optionalEntity = repository.findFirstById(UUID.fromString(id));
        final CreditAnalysisEntity entity = optionalEntity.orElseThrow(
                () -> new CreditAnalysisNotFoundException(String.format("Credit analysis not found with id %s", id)));

        return creditAnalysisMapper.from(entity);
    }
}
