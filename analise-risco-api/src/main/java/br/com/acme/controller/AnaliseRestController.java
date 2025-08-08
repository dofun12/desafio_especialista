package br.com.acme.controller;

import br.com.acme.dto.RequestAnaliseRiscoDto;
import br.com.acme.dto.ResponseAnaliseRiscoDto;
import br.com.acme.service.AnaliseRiscoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analise_risco")
public class AnaliseRestController {
    private final AnaliseRiscoService analiseRiscoService;

    public AnaliseRestController(AnaliseRiscoService analiseRiscoService) {
        this.analiseRiscoService = analiseRiscoService;
    }

    @PostMapping("/")
    public ResponseEntity<ResponseAnaliseRiscoDto> getAnaliseScore(@Valid @RequestBody RequestAnaliseRiscoDto requestAnaliseRiscoDto) {
        return new ResponseEntity<>(analiseRiscoService.getAnaliseRisco(requestAnaliseRiscoDto), HttpStatus.CREATED);
    }
}
