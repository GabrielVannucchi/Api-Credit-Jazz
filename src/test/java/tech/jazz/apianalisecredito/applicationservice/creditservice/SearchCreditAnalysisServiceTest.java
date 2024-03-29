package tech.jazz.apianalisecredito.applicationservice.creditservice;

import feign.FeignException;
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
import tech.jazz.apianalisecredito.presentation.controller.CreditController;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SearchCreditAnalysisServiceTest {
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
    private SearchCreditAnalysisService service;

    @Captor
    private ArgumentCaptor<String> clientIdCaptor;
    @Captor
    private ArgumentCaptor<String> clientCpfCaptor;
    @Captor
    private ArgumentCaptor<UUID> analysisId;

    @Test
    void should_throw_CliendNotFoundException_when_id_not_found_while_searching(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenThrow(FeignException.class);

        assertThrows(ClientNotFoundException.class,() -> service.listAnalysisByClient("690cfa4d-2228-4343-85db-82e96e122da9"));
    }
    @Test
    void should_throw_CliendNotFoundException_when_cpf_not_found_while_searching(){
        Mockito.when(clientApi.getClientByCpf(clientCpfCaptor.capture())).thenThrow(FeignException.class);
        assertThrows(ClientNotFoundException.class,() -> service.listAnalysisByClient("012.345.678-90"));
        assertThrows(ClientNotFoundException.class,() -> service.listAnalysisByClient("45896853882"));
    }
    @Test
    void should_return_3_analysis_in_findAll(){
        Mockito.when(repository.findAll()).thenReturn(List.of(entityFactory(),entityFactory(),entityFactory()));
        List<AllAnalysisResponse> responses = service.listAllCreditAnalysis();
        assertEquals(3, responses.size());
    }
    @Test
    void should_return_client_by_CPF(){
        Mockito.when(clientApi.getClientByCpf(clientCpfCaptor.capture())).thenReturn(new ClientApiRequest("12341234-1234-1234-1234-123412341234"));
        Mockito.when(repository.findByClientId("12341234-1234-1234-1234-123412341234")).thenReturn(List.of(entityFactory(),entityFactory()));
        List<ClientAnalysisResponse> responses = service.listAnalysisByClient("01234567890");
        assertEquals(2, responses.size());
    }
    @Test
    void should_return_client_by_UUID(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenReturn(new ClientApiRequest("12341234-1234-1234-1234-123412341234"));
        Mockito.when(repository.findByClientId("12341234-1234-1234-1234-123412341234")).thenReturn(List.of(entityFactory(),entityFactory()));
        List<ClientAnalysisResponse> responses = service.listAnalysisByClient("12341234-1234-1234-1234-123412341234");
        assertEquals(2, responses.size());
    }
    @Test
    void should_throw_ClientNotFoundException_when_client_CPF_dont_exists_in_clientApi(){
        Mockito.when(clientApi.getClientByCpf(clientCpfCaptor.capture())).thenThrow(ClientNotFoundException.class);
        assertThrows(ClientNotFoundException.class, () -> service.listAnalysisByClient("01234567890"));
    }
    @Test
    void should_throw_ClientParamOutOfFormatException_when_client_params_format_is_incorrect(){
        assertThrows(ClientParamOutOfFormatException.class, () -> service.listAnalysisByClient("1"));
    }
    @Test
    void should_throw_ClientNotFoundException_when_client_UUID_dont_exists_in_clientApi(){
        Mockito.when(clientApi.getClientById(clientIdCaptor.capture())).thenThrow(ClientNotFoundException.class);
        assertThrows(ClientNotFoundException.class, () -> service.listAnalysisByClient("12341234-1234-1234-1234-123412341234"));
    }
    @Test
    void should_return_credit_analysis(){
        Mockito.when(repository.findFirstById(analysisId.capture())).thenReturn(Optional.of(entityFactory()));
        ClientAnalysisResponse response = service.findAnalysisById("12341234-1234-1234-1234-123412341234");
        assertNotNull(response);
        assertNotNull(response.id());
    }
    @Test
    void should_throw_CreditAnalysisNotFoundException_when_UUID_dont_exists_in_repository(){
        Mockito.when(repository.findFirstById(analysisId.capture())).thenReturn(Optional.empty());
        assertThrows(CreditAnalysisNotFoundException.class,() -> service.findAnalysisById("12341234-1234-1234-1234-123412341234"));
    }
    @Test
    void should_throw_UuidOutOfFormatException_when_UUID_is_invalid(){
        assertThrows(UuidOutOfFormatException.class, () -> service.findAnalysisById("12341"));
    }

    private CreditAnalysisEntity entityFactory(){
        return CreditAnalysisEntity.builder()
                .approved(true)
                .approvedLimit(new BigDecimal(20))
                .requestedAmount(new BigDecimal(20))
                .withdraw(new BigDecimal(20))
                .annualInterest(15f)
                .clientId("12341234-1234-1234-1234-123412341234")
                .date(LocalDateTime.of(1997,4,10,4,20))
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