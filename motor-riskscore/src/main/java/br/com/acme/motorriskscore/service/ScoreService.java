package br.com.acme.motorriskscore.service;

import br.com.acme.motorriskscore.model.ScoreAction;
import br.com.acme.motorriskscore.model.ScoreCondition;
import br.com.acme.motorriskscore.model.ScoreRule;
import br.com.acme.motorriskscore.repository.ScoreActionRepository;
import br.com.acme.motorriskscore.repository.ScoreConditionRepository;
import br.com.acme.motorriskscore.repository.ScoreRuleJDBCRepository;
import br.com.acme.motorriskscore.repository.ScoreRuleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreService {
    private final ScoreRuleRepository scoreRuleRepository;
    private final ScoreConditionRepository scoreConditionRepository;
    private final ScoreActionRepository scoreActionRepository;
    private final ScoreRuleJDBCRepository scoreRuleJDBCRepository;

    public ScoreService(ScoreRuleRepository scoreRuleRepository,
                        ScoreConditionRepository scoreConditionRepository,
                        ScoreActionRepository scoreActionRepository,
                        ScoreRuleJDBCRepository scoreRuleJDBCRepository) {
        this.scoreRuleRepository = scoreRuleRepository;
        this.scoreConditionRepository = scoreConditionRepository;
        this.scoreActionRepository = scoreActionRepository;
        this.scoreRuleJDBCRepository = scoreRuleJDBCRepository;
    }

    @Cacheable(value = "ruleConditionCount", key = "#ruleIds")
    public Map<Long, Integer> getRuleConditionCount(Set<Long> ruleIds) {
        return scoreRuleJDBCRepository.getRuleConditionCount(ruleIds);
    }

    public ScoreRule addRule(String ruleName, String ruleDescription) {
        ScoreRule defaultRule = new ScoreRule();
        defaultRule.setName(ruleName);
        defaultRule.setDescription(ruleDescription);
        defaultRule.setEnabled(true);
        return scoreRuleRepository.save(defaultRule);
    }

    public void addAction(ScoreRule scoreRule, String field, String actionType, String value) {
        ScoreAction scoreAction = new ScoreAction();
        scoreAction.setField(field);
        scoreAction.setActionType(actionType);
        scoreAction.setValue(value);
        scoreAction.setRule(scoreRule);
        scoreActionRepository.save(scoreAction);
    }

    @Cacheable(value = "scoreConditions")
    public List<ScoreCondition> getAllConditions(){
        return scoreConditionRepository.findAll();
    }

    public ScoreRule getRuleById(Long ruleId) {
        return scoreRuleRepository.findById(ruleId).orElse(null);
    }

    public List<ScoreAction> getActionsByRuleId(Long ruleId){
        return scoreActionRepository.searchAllByRuleId(ruleId);
    }


    public void addCondition(ScoreRule scoreRule, String field, String conditionType, String value) {
        ScoreCondition scoreCondition = new ScoreCondition();
        scoreCondition.setField(field);
        scoreCondition.setConditionType(conditionType);
        scoreCondition.setValue(value);
        scoreCondition.setRule(scoreRule);
        scoreConditionRepository.save(scoreCondition);
    }


}