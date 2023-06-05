package tech.jazz.apianalisecredito.applicationservice.creditservice;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.jazz.apianalisecredito.applicationservice.dto.ClientApiRequest;

//@CircuitBreaker(name = "getClientCB")
@FeignClient(name = "ClientApi", url = "localhost:8080/clients")
public interface ClientApi {
    @GetMapping("/?id={id}")
    ClientApiRequest getClientById(@PathVariable UUID id);

    @GetMapping("/?cpf={cpf}")
    ClientApiRequest getClientByCpf(@PathVariable String cpf);

}
