package br.com.acme.motorriskscore.controller;

import br.com.acme.motorriskscore.service.ScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
    private final ScoreService scoreService;

    final ObjectMapper mapper = new ObjectMapper();
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping("/calculate")
    public ObjectNode calculateScore(@Valid @RequestBody ObjectNode requestAnaliseRiscoDto) {
        return scoreService.getScore(requestAnaliseRiscoDto);
    }
}
