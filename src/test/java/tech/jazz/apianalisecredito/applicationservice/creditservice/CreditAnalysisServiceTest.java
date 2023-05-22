package tech.jazz.apianalisecredito.applicationservice.creditservice;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import tech.jazz.apianalisecredito.applicationservice.dto.ClientApiRequest;
import tech.jazz.apianalisecredito.infrastructure.mapper.AllAnalysisMapper;
import tech.jazz.apianalisecredito.infrastructure.mapper.AllAnalysisMapperImpl;
import tech.jazz.apianalisecredito.infrastructure.mapper.CreditAnalysisMapper;
import tech.jazz.apianalisecredito.infrastructure.mapper.CreditAnalysisMapperImpl;
import tech.jazz.apianalisecredito.infrastructure.repository.CreditAnalysisRepository;
import tech.jazz.apianalisecredito.infrastructure.repository.entity.CreditAnalysisEntity;
import tech.jazz.apianalisecredito.presentation.dto.request.CreditAnalysisRequest;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreditAnalysisServiceTest {
    @Spy
    private AllAnalysisMapper allAnalysisMapper = new AllAnalysisMapperImpl();
    @Spy
    private CreditAnalysisMapper creditAnalysisMapper = new CreditAnalysisMapperImpl();
    @Mock
    private ClientApi clientApi;
    @Mock
    private CreditAnalysisRepository repository;
    @InjectMocks
    private CreditAnalysisService service;

    @Captor
    private ArgumentCaptor<String> clientIdCaptor;
    @Test
    void should_create_analysis(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(new ClientApiRequest("690cfa4d-2228-4343-85db-82e96e122da5"));
        Mockito.when(repository.save(Mockito.any(CreditAnalysisEntity.class)))
                .thenAnswer(new Answer<CreditAnalysisEntity>() {
                    public CreditAnalysisEntity answer(InvocationOnMock invocation) throws  Throwable{
                        Object[] arguments = invocation.getArguments();

                        if (arguments != null && arguments.length > 0 && arguments[0] instanceof CreditAnalysisEntity){
                            CreditAnalysisEntity objEntity = (CreditAnalysisEntity) arguments[0];
                            objEntity = CreditAnalysisEntity.builder()
                                    .approved(objEntity.getApproved())
                                    .approvedLimit(objEntity.getApprovedLimit())
                                    .requestedAmount(objEntity.getRequestedAmount())
                                    .withdraw(objEntity.getWithdraw())
                                    .annualInterest(objEntity.getAnnualInterest())
                                    .clientId(objEntity.getClientId())
                                    .date(objEntity.getDate())
                                    .build();
                            return objEntity;
                        }
                        return  null;

                    }
                });
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId("690cfa4d-2228-4343-85db-82e96e122da5")
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(5000))
                .build();

        ClientAnalysisResponse response = service.createAnalysis(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(new BigDecimal(6000).setScale(2, RoundingMode.DOWN),response.approvedLimit());

    }
}