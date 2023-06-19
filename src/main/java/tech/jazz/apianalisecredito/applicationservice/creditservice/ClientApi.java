package tech.jazz.apianalisecredito.applicationservice.creditservice;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tech.jazz.apianalisecredito.applicationservice.dto.ClientApiRequest;

//@CircuitBreaker(name = "getClientCB")
@FeignClient(name = "ClientApi", url = "${client-api-path}")
public interface ClientApi {

    @GetMapping
    List<ClientApiRequest> getClientCpf(@RequestParam String cpf);

    @GetMapping("{id}")
    ClientApiRequest getClientId(@PathVariable UUID id);

}
