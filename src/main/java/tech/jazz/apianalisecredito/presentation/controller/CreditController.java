package tech.jazz.apianalisecredito.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tech.jazz.apianalisecredito.applicationservice.creditservice.CreditAnalysisService;
import tech.jazz.apianalisecredito.presentation.dto.request.CreditAnalysisRequest;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;

@RestController
@RequestMapping("credit")
@RequiredArgsConstructor
public class CreditController {
    private final CreditAnalysisService service;

    // @Valid não é necessário
    @PostMapping("analysis")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ClientAnalysisResponse creditAnalysis(@RequestBody @Valid CreditAnalysisRequest creditRequest) {
        return service.createAnalysis(creditRequest);
    }

    @GetMapping("analysis")
    public List<AllAnalysisResponse> listAllAnalysis() {
        return service.listAllCreditAnalysis();
    }

    // A consulta por cpf ou id de client são filtros utilizando query paramenters,
    // aqui esta utilizando path variable, este endpoint e o metodo em serviço precisa ser restruturado
    // Tb é incorreto receber um parementro que tem dois significados
    @GetMapping("analysis/client/{param}")
    public List<ClientAnalysisResponse> listAnalysisByClient(@PathVariable String param) {
        return service.listAnalysisByClient(param);
    }

    // utilizar o tipo de dado correto, uuid
    @GetMapping("analysis/{id}")
    public ClientAnalysisResponse findAnalysis(@PathVariable String id) {
        return service.findAnalysisById(id);
    }
}
