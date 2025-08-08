package br.com.acme.motorriskscore.service;

import br.com.acme.motorriskscore.model.ScoreAction;
import br.com.acme.motorriskscore.model.ScoreCondition;
import br.com.acme.motorriskscore.model.ScoreRule;
import br.com.acme.motorriskscore.repository.ScoreActionRepository;
import br.com.acme.motorriskscore.repository.ScoreConditionRepository;
import br.com.acme.motorriskscore.repository.ScoreRuleJDBCRepository;
import br.com.acme.motorriskscore.repository.ScoreRuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScoreService {
    private final ScoreRuleRepository scoreRuleRepository;
    private final ScoreConditionRepository scoreConditionRepository;
    private final ScoreActionRepository scoreActionRepository;
    private final ScoreRuleJDBCRepository scoreRuleJDBCRepository;

    public ScoreService(ScoreRuleRepository scoreRuleRepository,
                        ScoreConditionRepository scoreConditionRepository,
                        ScoreActionRepository scoreActionRepository, ScoreRuleJDBCRepository scoreRuleJDBCRepository) {
        this.scoreRuleRepository = scoreRuleRepository;
        this.scoreConditionRepository = scoreConditionRepository;
        this.scoreActionRepository = scoreActionRepository;
        this.scoreRuleJDBCRepository = scoreRuleJDBCRepository;


        addDefaultRules();

    }

    private void addDefaultRules(){
        ScoreRule scoreRuleA = addRule("Baixa transacao", "Regra para transacoes de baixo valor");
        addCondition(scoreRuleA, "tx_value", "GREATER_THAN", "0.01");
        addCondition(scoreRuleA, "tx_value", "LESS_THAN", "300.00");
        addAction(scoreRuleA, "tx_score", "ADD", "200");

        ScoreRule scoreRuleB = addRule("Razoavel transacao", "Regra para transacoes de razoavel valor");
        addCondition(scoreRuleB, "tx_value", "GREATER_THAN", "300.00");
        addCondition(scoreRuleB, "tx_value", "LESS_THAN", "5000.00");
        addAction(scoreRuleB, "tx_score", "ADD", "300");

        ScoreRule scoreRuleC = addRule("Alta transacao", "Regra para transacoes de alto valor");
        addCondition(scoreRuleC, "tx_value", "GREATER_THAN", "5000.01");
        addCondition(scoreRuleC, "tx_value", "LESS_THAN", "20000.00");
        addAction(scoreRuleC, "tx_score", "ADD", "400");

        ScoreRule scoreRuleD = addRule("Super Alta transacao", "Regra para transacoes de muito alto valor");
        addCondition(scoreRuleD, "tx_value", "GREATER_THAN", "20000");
        addAction(scoreRuleC, "tx_score", "ADD", "500");


    }

    private void addSomeRules() {
        ScoreRule defaultRule = new ScoreRule();
        defaultRule.setName("Default Rule");
        defaultRule.setDescription("This is a default rule for scoring.");
        defaultRule.setEnabled(true);
        scoreRuleRepository.save(defaultRule);


        ScoreRule ruleRestric = new ScoreRule();
        ruleRestric.setName("Restrict CPF, CNPJ, DEVICE_ID");
        ruleRestric.setDescription("This rule restricts transactions based on CPF, CNPJ, and DEVICE_ID.");
        ruleRestric.setEnabled(true);
        scoreRuleRepository.save(ruleRestric);

        addCondition(ruleRestric, "restrictions", "CONTAINS", "CPF");
        addAction(ruleRestric, "tx_score", "SUBTRACT", "10");

        addCondition(defaultRule, "tx_value", "EQUALS", "10");
        addCondition(defaultRule, "tx_value", "BETWEEN", "10,100");

        addAction(defaultRule, "tx_score", "ADD", "100");


        ObjectNode transaction = new ObjectMapper().createObjectNode();
        transaction.put("restrictions", "CPF, CNPJ, DEVICE_ID");
        transaction.put("tx_value", "10");
        transaction.put("tx_score", 0);
        var value = getScore(transaction);
        System.out.println(value);
    }

    private ScoreRule addRule(String ruleName, String ruleDescription) {
        ScoreRule defaultRule = new ScoreRule();
        defaultRule.setName(ruleName);
        defaultRule.setDescription(ruleDescription);
        defaultRule.setEnabled(true);
        return scoreRuleRepository.save(defaultRule);
    }

    private void addAction(ScoreRule scoreRule, String field, String actionType, String value) {
        ScoreAction scoreAction = new ScoreAction();
        scoreAction.setField(field);
        scoreAction.setActionType(actionType);
        scoreAction.setValue(value);
        scoreAction.setRule(scoreRule);
        scoreActionRepository.save(scoreAction);
    }

    private void addCondition(ScoreRule scoreRule, String field, String conditionType, String value) {
        ScoreCondition scoreCondition = new ScoreCondition();
        scoreCondition.setField(field);
        scoreCondition.setConditionType(conditionType);
        scoreCondition.setValue(value);
        scoreCondition.setRule(scoreRule);
        scoreConditionRepository.save(scoreCondition);
    }

    private boolean isConditionMet(ScoreCondition condition, ObjectNode transaction) {
        if(condition==null || condition.getField()==null) {
            return false;
        }
        if(transaction == null) {
            return false;
        }
        if(!transaction.has(condition.getField())) {

            return false;
        }
        String originValue = transaction.get(condition.getField()).asText();
        String targetValue = condition.getValue();

        switch (condition.getConditionType()) {
            case "EQUALS":
                return originValue.equals(targetValue);
            case "NOT_EQUALS":
                return !originValue.equals(targetValue);
            case "GREATER_THAN":
                return Double.parseDouble(originValue) > Double.parseDouble(targetValue);
            case "LESS_THAN":
                return Double.parseDouble(originValue) < Double.parseDouble(targetValue);
            // Add more conditions as needed
            case "CONTAINS":
                return originValue.contains(targetValue);
            case "NOT_CONTAINS":
                return !originValue.contains(targetValue);
            case "BETWEEN":
                String[] range = targetValue.split(",");
                if (range.length != 2) {
                    throw new IllegalArgumentException("Invalid range format for BETWEEN condition");
                }
                double lowerBound = Double.parseDouble(range[0]);
                double upperBound = Double.parseDouble(range[1]);
                double value = Double.parseDouble(originValue);
                return value >= lowerBound && value <= upperBound;
            default:
                return false;
        }
    }
    private ObjectNode runAction(ScoreAction scoreAction, ObjectNode transaction) {
        String field = scoreAction.getField();
        String actionType = scoreAction.getActionType();
        String value = scoreAction.getValue();

        if(!transaction.has(field)){
            transaction.put(field, 0);
        }

        switch (actionType) {
            case "ADD":
                double currentValue = transaction.get(field).asDouble(0);
                double newValue = currentValue + Double.parseDouble(value);
                transaction.put(field, newValue);
                return transaction;
            case "SUBTRACT":
                double currentValueSub = transaction.get(field).asDouble(0);
                double newValueSub = currentValueSub - Double.parseDouble(value);
                transaction.put(field, newValueSub);
                return transaction;
            // Add more actions as needed
            default:
                //throw new IllegalArgumentException("Unknown action type: " + actionType);
                return transaction;
        }
    }

    private ObjectNode runActions(ScoreRule rule, ObjectNode transaction) {
        if (rule== null || !rule.isEnabled()) {
            return transaction;
        }
        List<ScoreAction> actions = scoreActionRepository.searchAllByRuleId(rule.getId());
        for (ScoreAction action : actions) {
           runAction(action, transaction);
        }
        return transaction;
    }

    public ObjectNode getScore(ObjectNode transaction) {
        List<ScoreCondition> conditions = scoreConditionRepository.findAll();
        Map<Long, Integer> matchedRulesConditions = new HashMap<>();
        for (ScoreCondition condition : conditions) {
            if(isConditionMet(condition, transaction)) {
                Long ruleId = condition.getRule().getId();
                matchedRulesConditions.put(ruleId, matchedRulesConditions.getOrDefault(ruleId, 0) + 1);
            }
        }
        Map<Long, Integer> allRulesConditionCount = scoreRuleJDBCRepository.getRuleConditionCount(matchedRulesConditions.keySet());
        for (Map.Entry<Long, Integer> entry : matchedRulesConditions.entrySet()) {
            Long ruleId = entry.getKey();
            Integer matchedCount = entry.getValue();
            Integer totalCount = allRulesConditionCount.get(ruleId);
            if (totalCount != null && Objects.equals(matchedCount,totalCount)) {
                runActions(scoreRuleRepository.findById(ruleId).orElse(null), transaction);
            }
        }
        return transaction;
    }

    public void addRule(ScoreRule scoreRule) {
        scoreRuleRepository.save(scoreRule);
    }

}
