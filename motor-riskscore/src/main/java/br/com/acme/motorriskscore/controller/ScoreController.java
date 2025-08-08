package br.com.acme.motorriskscore.controller;

import br.com.acme.motorriskscore.service.ScoreCalculateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
    private final ScoreCalculateService scoreCalculateService;

    final ObjectMapper mapper = new ObjectMapper();
    public ScoreController(ScoreCalculateService scoreService) {
        this.scoreCalculateService = scoreService;
    }

    @PostMapping("/calculate")
    public ObjectNode calculateScore(@Valid @RequestBody ObjectNode requestAnaliseRiscoDto) {
        return scoreCalculateService.getScore(requestAnaliseRiscoDto);
    }
}
