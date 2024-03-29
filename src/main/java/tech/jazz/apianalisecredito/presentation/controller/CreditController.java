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
import tech.jazz.apianalisecredito.applicationservice.creditservice.CreateCreditAnalysisService;
import tech.jazz.apianalisecredito.applicationservice.creditservice.SearchCreditAnalysisService;
import tech.jazz.apianalisecredito.presentation.dto.request.CreditAnalysisRequest;
import tech.jazz.apianalisecredito.presentation.dto.response.AllAnalysisResponse;
import tech.jazz.apianalisecredito.presentation.dto.response.ClientAnalysisResponse;

@RestController
@RequestMapping("credit")
@RequiredArgsConstructor
public class CreditController {
    private final CreateCreditAnalysisService createService;
    private final SearchCreditAnalysisService searchService;

    @PostMapping("analysis")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ClientAnalysisResponse creditAnalysis(@RequestBody @Valid CreditAnalysisRequest creditRequest) {
        return createService.createAnalysis(creditRequest);
    }

    @GetMapping("analysis")
    public List<AllAnalysisResponse> listAllAnalysis() {
        return searchService.listAllCreditAnalysis();
    }

    @GetMapping("analysis/client/{param}")
    public List<ClientAnalysisResponse> listAnalysisByClient(@PathVariable String param) {
        return searchService.listAnalysisByClient(param);
    }

    @GetMapping("analysis/{id}")
    public ClientAnalysisResponse findAnalysis(@PathVariable String id) {
        return searchService.findAnalysisById(id);
    }
}
