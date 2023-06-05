package tech.jazz.apianalisecredito.applicationservice.creditservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import feign.FeignException;
import feign.RetryableException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
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
import tech.jazz.apianalisecredito.presentation.controller.CreditController;
import tech.jazz.apianalisecredito.presentation.dto.request.CreditAnalysisRequest;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientApiUnavailableException;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.ClientNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateCreditAnalysisServiceTest {
    @Spy
    private AllAnalysisMapper allAnalysisMapper = new AllAnalysisMapperImpl();
    @Spy
    private CreditAnalysisMapper creditAnalysisMapper = new CreditAnalysisMapperImpl();
    @Mock
    private ClientApi clientApi;
    @Mock
    private CreditAnalysisRepository repository;
    @Mock
    private CreditController controller;
    @InjectMocks
    private CreateCreditAnalysisService service;

    @Captor
    private ArgumentCaptor<UUID> clientIdCaptor;
    @Captor
    private ArgumentCaptor<String> clientCpfCaptor;
    @Captor
    private ArgumentCaptor<UUID> analysisId;

    @Test
    void should_create_analysis(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(new ClientApiRequest("690cfa4d-2228-4343-85db-82e96e122da5"));
        whenSaveConfiguration();
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId(UUID.randomUUID())
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(5000))
                .build();

        ClientAnalysisResponse response = service.createAnalysis(request);

        assertNotNull(response);
        assertNotNull(response.id());
    }
    @Test
    void should_approve_analysis(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(new ClientApiRequest("690cfa4d-2228-4343-85db-82e96e122da5"));
        whenSaveConfiguration();
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId(UUID.randomUUID())
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(5000))
                .build();

        ClientAnalysisResponse response = service.createAnalysis(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertTrue(response.approved());
    }
    @Test
    void should_approve_analysis_with_30percent_of_monthlyIncome(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(new ClientApiRequest("690cfa4d-2228-4343-85db-82e96e122da5"));
        whenSaveConfiguration();
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId(UUID.randomUUID())
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(5000))
                .build();

        ClientAnalysisResponse response = service.createAnalysis(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertTrue(response.approved());
        assertEquals(new BigDecimal(6000).setScale(2, RoundingMode.DOWN), response.approvedLimit().setScale(2, RoundingMode.DOWN));
        assertEquals(new BigDecimal(600).setScale(2, RoundingMode.DOWN), response.withdraw().setScale(2, RoundingMode.DOWN));
    }
    @Test
    void should_approve_analysis_with_15percent_of_monthlyIncome(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(new ClientApiRequest("690cfa4d-2228-4343-85db-82e96e122da5"));
        whenSaveConfiguration();
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId(UUID.randomUUID())
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(11000))
                .build();

        ClientAnalysisResponse response = service.createAnalysis(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertTrue(response.approved());
        assertEquals(new BigDecimal(3000).setScale(2, RoundingMode.DOWN), response.approvedLimit().setScale(2, RoundingMode.DOWN));
        assertEquals(new BigDecimal(300).setScale(2, RoundingMode.DOWN), response.withdraw().setScale(2, RoundingMode.DOWN));
    }
    @Test
    void should_approve_analysis_with_rounding_monthlyIncome_to_50000(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(new ClientApiRequest("690cfa4d-2228-4343-85db-82e96e122da5"));
        whenSaveConfiguration();
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId(UUID.randomUUID())
                .monthlyIncome(new BigDecimal(2000000))
                .requestedAmount(new BigDecimal(5000))
                .build();

        ClientAnalysisResponse response = service.createAnalysis(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertTrue(response.approved());
        assertEquals(new BigDecimal(15000).setScale(2, RoundingMode.DOWN), response.approvedLimit().setScale(2, RoundingMode.DOWN));
        assertEquals(new BigDecimal(1500).setScale(2, RoundingMode.DOWN), response.withdraw().setScale(2, RoundingMode.DOWN));
    }
    @Test
    void should_create_unapproved_analysis(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(new ClientApiRequest("690cfa4d-2228-4343-85db-82e96e122da5"));
        whenSaveConfiguration();
        CreditAnalysisRequest request = CreditAnalysisRequest.builder()
                .clientId(UUID.randomUUID())
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(50000))
                .build();

        ClientAnalysisResponse response = service.createAnalysis(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertFalse(response.approved());
        assertEquals(new BigDecimal(0).setScale(2, RoundingMode.DOWN), response.approvedLimit().setScale(2, RoundingMode.DOWN));
        assertEquals(new BigDecimal(0).setScale(2, RoundingMode.DOWN), response.withdraw().setScale(2, RoundingMode.DOWN));

    }
    @Test
    void should_throw_ClientNotFoundException_when_id_not_found_while_creating(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenThrow(FeignException.class);
        assertThrows(ClientNotFoundException.class,() -> service.createAnalysis(genericCreditAnalysisRequestFactory()));
    }
    @Test
    void should_throw_ClientApiUnavailableException_when_RetryableException_is_throw_by_ClientApi(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenThrow(RetryableException.class);
        assertThrows(ClientApiUnavailableException.class, () -> service.createAnalysis(genericCreditAnalysisRequestFactory()));
    }

    private CreditAnalysisRequest genericCreditAnalysisRequestFactory(){
        return CreditAnalysisRequest.builder()
                .clientId(UUID.randomUUID())
                .monthlyIncome(new BigDecimal(20000))
                .requestedAmount(new BigDecimal(50000))
                .build();
    }
    private void whenSaveConfiguration(){
        Mockito.when(repository.save(Mockito.any(CreditAnalysisEntity.class)))
                .thenAnswer(new Answer<CreditAnalysisEntity>() {
                    public CreditAnalysisEntity answer(InvocationOnMock invocation) throws Throwable{
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
    }
}