package tech.jazz.apianalisecredito.applicationservice.creditservice;

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
import tech.jazz.apianalisecredito.presentation.handler.exceptions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CreditAnalysisService {
    private final CreditAnalysisRepository repository;
    private final CreditAnalysisMapper creditAnalysisMapper;
    private final AllAnalysisMapper allAnalysisMapper;
    private final ClientApi clientApi;
    private final BigDecimal maxIncome = BigDecimal.valueOf(50000.00);
    private final String UUIDRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final String CPFRegex = "(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})";
    private final Integer annualInterest = 15;
    private final float highRequestPercent = 15.0f;
    private final float lowRequestPercent = 30.0f;
    private final float withdrawPercent = 10.0f;
    public ClientAnalysisResponse createAnalysis(CreditAnalysisRequest request) {
        //value config
        BigDecimal incomeValue = request.monthlyIncome();
        BigDecimal withdraw = new BigDecimal(0);
        BigDecimal approvedLimit = new BigDecimal(0);
        boolean approved;

        //end value config
        //api client
        try {
            clientApi.getClientById(request.clientId());
        } catch (Exception e) {
            throw new ClientNotFoundException();
        }
        //end api client
        //regras de negocio

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

        CreditAnalysis creditAnalysis = CreditAnalysis.builder()
                .approved(approved)
                .approvedLimit(approvedLimit)
                .requestedAmount(request.requestedAmount())
                .withdraw(withdraw)
                .annualInterest(annualInterest)
                .clientId(UUID.fromString(request.clientId()))
                .date(LocalDateTime.now())
                .build();

        CreditAnalysisEntity entity = creditAnalysisMapper.from(creditAnalysis);
        return creditAnalysisMapper.from(repository.save(entity));
    }

    public List<AllAnalysisResponse> listAllCreditAnalysis() {
        List<CreditAnalysisEntity> listEntity = repository.findAll();
        List<AllAnalysisResponse> listResponse = new ArrayList<>();
        for (CreditAnalysisEntity entity :
                listEntity) {
            listResponse.add(allAnalysisMapper.from(entity));
        }

        return listResponse;
    }

    public List<ClientAnalysisResponse> listAnalysisByClient(String param) {

        if(Pattern.matches(CPFRegex, param)){
            try {
                ClientApiRequest client = clientApi.getClientByCpf(param);
                param = client.id();
            } catch (Exception e) {
                throw new ClientNotFoundException();
            }
        }else if(!Pattern.matches(UUIDRegex, param)){
            throw new ClientParamOutOfFormatException();
        }else{
            try {
                ClientApiRequest client = clientApi.getClientById(param);
            } catch (Exception e) {
                throw new ClientNotFoundException();
            }
        }
        List<CreditAnalysisEntity> listEntity  = repository.findByClientId(param);
        List<ClientAnalysisResponse> listResponse = new ArrayList<>();
        for (CreditAnalysisEntity entity :
                listEntity) {
            listResponse.add(creditAnalysisMapper.from(entity));
        }
        return listResponse;
    }

    public ClientAnalysisResponse findAnalysisById(String id) {
        if(!Pattern.matches(UUIDRegex, id)){
            throw new UuidOutOfFormatException();
        }
        Optional<CreditAnalysisEntity> optionalEntity= repository.findFirstById(UUID.fromString(id));
        CreditAnalysisEntity entity = optionalEntity.orElseThrow(() -> new CreditAnalysisNotFoundException());

        return creditAnalysisMapper.from(entity);
    }


}
