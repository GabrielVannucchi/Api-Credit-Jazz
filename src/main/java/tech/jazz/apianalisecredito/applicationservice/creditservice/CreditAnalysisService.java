package tech.jazz.apianalisecredito.applicationservice.creditservice;

import feign.FeignException;
import feign.RetryableException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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
import tech.jazz.apianalisecredito.infrastructure.model.CreditAnalysis;
import tech.jazz.apianalisecredito.infrastructure.repository.CreditAnalysisRepository;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.request.CreditAnalysisRequest;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientApiUnavailableException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientNotFoundException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientParamOutOfFormatException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.CreditAnalysisNotFoundException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.UuidOutOfFormatException;


@Service
@RequiredArgsConstructor
public class CreditAnalysisService {
    private final CreditAnalysisRepository repository;
    private final CreditAnalysisMapper creditAnalysisMapper;
    private final AllAnalysisMapper allAnalysisMapper;
    private final ClientApi clientApi;
    // O padrão de nome das constantes estão fora do padrão da linguagem, Upper case EXAMPLE_CONSTANTS
    private final String uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final String cpfregex = "(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})";
    private final BigDecimal maxIncome = BigDecimal.valueOf(50000.00);
    private final float annualInterest = 15.0f;
    private final float highRequestPercent = 15.0f;
    private final float lowRequestPercent = 30.0f;
    private final float withdrawPercent = 10.0f;
    private final String clientNotFoundMessage = "Client not found in ClientApi";
    private final String clientApiUnavailableMessage = "Client API unavailable";

    public ClientAnalysisResponse createAnalysis(CreditAnalysisRequest request) {
        //value config
        BigDecimal incomeValue = request.monthlyIncome();
        BigDecimal withdraw = new BigDecimal(0);
        BigDecimal approvedLimit = new BigDecimal(0);
        final boolean approved;

        //end value config
        //api client
        try {
            clientApi.getClientById(request.clientId());
        } catch (RetryableException e) {
            throw new ClientApiUnavailableException(clientApiUnavailableMessage);
        } catch (FeignException e) {
            throw new ClientNotFoundException(clientNotFoundMessage);
        }
        //end api client
        //regras de negocio

        // Dica, coloque as condiçoes em métodos com nome claro, facilita a leitura do codigo
        // exemplo: isAmountRequestGreaterThanMonthlyIncome
        if (request.requestedAmount().compareTo(request.monthlyIncome()) > 0) {
            approved = false;
        } else {
            approved = true;
            if (request.monthlyIncome().compareTo(maxIncome) > 0) {
                incomeValue = maxIncome;
            }
            if (request.requestedAmount().compareTo(request.monthlyIncome().multiply(BigDecimal.valueOf(0.5))) == 1) {
                approvedLimit = incomeValue.multiply(BigDecimal.valueOf(highRequestPercent / 100));

            } else {
                approvedLimit = incomeValue.multiply(BigDecimal.valueOf(lowRequestPercent / 100));
            }
            approvedLimit = approvedLimit.setScale(2, RoundingMode.DOWN);
            withdraw = approvedLimit.multiply(BigDecimal.valueOf(withdrawPercent / 100)).setScale(2, RoundingMode.DOWN);
        }

        //end regras de negocio

        final CreditAnalysis creditAnalysis = CreditAnalysis.builder()
                .approved(approved)
                .approvedLimit(approvedLimit)
                .requestedAmount(request.requestedAmount())
                .withdraw(withdraw)
                .annualInterest(annualInterest)
                .clientId(UUID.fromString(request.clientId()))
                .date(LocalDateTime.now())
                .build();

        final CreditAnalysisEntity entity = creditAnalysisMapper.from(creditAnalysis);
        return creditAnalysisMapper.from(repository.save(entity));
    }

    public List<AllAnalysisResponse> listAllCreditAnalysis() {
        // uma alternativa aqui é utiliza stream, mas desta forma tb esta ok
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
                // este atributo não esta sendo utilizado
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
