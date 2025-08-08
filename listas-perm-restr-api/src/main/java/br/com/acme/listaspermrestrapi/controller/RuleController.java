package br.com.acme.listaspermrestrapi.controller;

import br.com.acme.listaspermrestrapi.dto.RuleDto;
import br.com.acme.listaspermrestrapi.dto.VerifyRuleDto;
import br.com.acme.listaspermrestrapi.model.RuleModel;
import br.com.acme.listaspermrestrapi.service.RuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rule")
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }


    @PostMapping("/allow")
    public ResponseEntity<List<RuleModel>> addAllowRule(@Valid @RequestBody RuleDto ruleDto) {
        List<RuleModel> savedRestriction = ruleService.addRule(ruleDto, true);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRestriction);
    }

    @PostMapping("/deny")
    public ResponseEntity<List<RuleModel>> addDenyRule(@Valid @RequestBody RuleDto ruleDto) {
        List<RuleModel> savedRestriction = ruleService.addRule(ruleDto, false);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRestriction);
    }

    @PostMapping("/validate")
    public ResponseEntity<VerifyRuleDto> validateRule(@Valid @RequestBody RuleDto ruleDto) {
        VerifyRuleDto validatedRules = ruleService.verifyRule(ruleDto);
        return ResponseEntity.ok(validatedRules);
    }

    @GetMapping("/")
    public ResponseEntity<List<RuleModel>> getAllRules() {
        List<RuleModel> rules = ruleService.getListAll();
        return ResponseEntity.ok(rules);
    }

}