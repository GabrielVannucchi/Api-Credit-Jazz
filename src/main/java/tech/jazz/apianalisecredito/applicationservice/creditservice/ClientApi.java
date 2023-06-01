package tech.jazz.apianalisecredito.applicationservice.creditservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.jazz.apianalisecredito.applicationservice.dto.ClientApiRequest;
import tech.jazz.apianalisecredito.presentation.dto.request.CreditAnalysisRequest;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.handler.exceptions.UuidOutOfFormatException;

@CircuitBreaker(name = "getClientCB", fallbackMethod = "getClientFallback")
@FeignClient(name = "ClientApi", url = "localhost:8080/clients/")
public interface ClientApi {

    @GetMapping("{id}")
    ClientApiRequest getClientById(@PathVariable String id);

    @GetMapping("cpf/{cpf}")
    ClientApiRequest getClientByCpf(@PathVariable String cpf);

    static ClientAnalysisResponse teste(CreditAnalysisRequest r, Throwable e) {
        throw new UuidOutOfFormatException("aaaa");
    }
}
