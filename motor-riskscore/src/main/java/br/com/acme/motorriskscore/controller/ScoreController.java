package br.com.acme.motorriskscore.controller;

import br.com.acme.motorriskscore.dto.ScoreRuleDto;
import br.com.acme.motorriskscore.model.ScoreRule;
import br.com.acme.motorriskscore.service.ScoreCalculateService;
import br.com.acme.motorriskscore.service.ScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
    private final ScoreCalculateService scoreCalculateService;
    private final ScoreService scoreService;


    final ObjectMapper mapper = new ObjectMapper();
    public ScoreController(ScoreCalculateService scoreService, ScoreService scoreService1) {
        this.scoreCalculateService = scoreService;
        this.scoreService = scoreService1;
    }

    @PostMapping("/calculate")
    public ObjectNode calculateScore(@Valid @RequestBody ObjectNode requestAnaliseRiscoDto) {
        return scoreCalculateService.getScore(requestAnaliseRiscoDto);
    }

    @PostMapping("/")
    public ResponseEntity<ScoreRuleDto> addRule(@Valid @RequestBody ScoreRuleDto scoreRule) {
        try {
            scoreService.addRule(scoreRule);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
