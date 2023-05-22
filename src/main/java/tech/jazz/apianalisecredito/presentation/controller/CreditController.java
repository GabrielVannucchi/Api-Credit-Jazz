package tech.jazz.apianalisecredito.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.jazz.apianalisecredito.applicationservice.creditservice.CreditAnalysisService;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.dto.request.CreditAnalysisRequest;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;

import java.util.List;

@RestController
@RequestMapping("credit")
@RequiredArgsConstructor
public class CreditController {
    private final CreditAnalysisService service;
    @PostMapping("analysis")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ClientAnalysisResponse creditAnalysis(@RequestBody @Valid CreditAnalysisRequest creditRequest){
        return service.createAnalysis(creditRequest);
    }

    @GetMapping("analysis")
    public List<AllAnalysisResponse> listAllAnalysis(){
        return service.listAllCreditAnalysis();
    }

    @GetMapping("analysis/client/{param}")
    public List<ClientAnalysisResponse> listAnalysisByClient(@PathVariable String param){
        return service.listAnalysisByClient(param);
    }

    @GetMapping("analysis/{id}")
    public ClientAnalysisResponse findAnalysis(@PathVariable String id){
        return service.findAnalysisById(id);
    }
}