package br.com.acme.restcontroller;

import br.com.acme.dto.RequestAnaliseRiscoDto;
import br.com.acme.dto.ResponseAnaliseRiscoDto;
import br.com.acme.service.AnaliseRiscoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analise_risco")
public class AnaliseRestController {
    private final AnaliseRiscoService analiseRiscoService;

    public AnaliseRestController(AnaliseRiscoService analiseRiscoService) {
        this.analiseRiscoService = analiseRiscoService;
    }

    @PostMapping("/")
    public ResponseAnaliseRiscoDto getAnaliseScore(@RequestBody RequestAnaliseRiscoDto requestAnaliseRiscoDto) {
        return analiseRiscoService.getAnaliseRisco(requestAnaliseRiscoDto);
    }
}
