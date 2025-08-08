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
public class ScoreCalculateService {

    // Rule names and descriptions for addDefaultRules
    private static final String RULE_BAIXA_NAME = "Baixa transacao";
    private static final String RULE_BAIXA_DESC = "Regra para transacoes de baixo valor";
    private static final String RULE_RAZOAVEL_NAME = "Razoavel transacao";
    private static final String RULE_RAZOAVEL_DESC = "Regra para transacoes de razoavel valor";
    private static final String RULE_ALTA_NAME = "Alta transacao";
    private static final String RULE_ALTA_DESC = "Regra para transacoes de alto valor";
    private static final String RULE_SUPER_ALTA_NAME = "Super Alta transacao";
    private static final String RULE_SUPER_ALTA_DESC = "Regra para transacoes de muito alto valor";

    // Common field names
    private static final String FIELD_TX_VALUE = "tx_value";
    private static final String FIELD_TX_SCORE = "tx_score";
    private static final String FIELD_RESTRICTIONS = "restrictions";

    // Condition types for addDefaultRules and addSomeRules
    private static final String CONDITION_GREATER_THAN_OR_EQUALS = "GREATER_THAN_OR_EQUALS";
    private static final String CONDITION_LESS_THAN_OR_EQUALS = "LESS_THAN_OR_EQUALS";
    private static final String CONDITION_GREATER_THAN = "GREATER_THAN";
    private static final String CONDITION_LESS_THAN = "LESS_THAN";
    private static final String CONDITION_EQUALS = "EQUALS";
    private static final String CONDITION_NOT_EQUALS = "NOT_EQUALS";
    private static final String CONDITION_BETWEEN = "BETWEEN";
    private static final String CONDITION_CONTAINS = "CONTAINS";
    private static final String CONDITION_NOT_CONTAINS = "NOT_CONTAINS";

    // Action types
    private static final String ACTION_ADD = "ADD";
    private static final String ACTION_SUBTRACT = "SUBTRACT";

    // Values for addDefaultRules
    private static final String VALUE_0_01 = "0.01";
    private static final String VALUE_300_00 = "300.00";
    private static final String VALUE_200 = "200";
    private static final String VALUE_5000_00 = "5000.00";
    private static final String VALUE_5000_01 = "5000.01";
    private static final String VALUE_20000_00 = "20000.00";
    private static final String VALUE_20000 = "20000";
    private static final String VALUE_500 = "500";

    final ObjectMapper mapper = new ObjectMapper();
    final ScoreService scoreService;

    public ScoreCalculateService(ScoreService scoreService) {
        this.scoreService = scoreService;

        addDefaultRules();
    }

    private void addDefaultRules() {
        ScoreRule scoreRuleA = scoreService.addRule(RULE_BAIXA_NAME, RULE_BAIXA_DESC);
        scoreService.addCondition(scoreRuleA, FIELD_TX_VALUE, CONDITION_GREATER_THAN_OR_EQUALS, VALUE_0_01);
        scoreService.addCondition(scoreRuleA, FIELD_TX_VALUE, CONDITION_LESS_THAN_OR_EQUALS, VALUE_300_00);
        scoreService.addAction(scoreRuleA, FIELD_TX_SCORE, ACTION_ADD, VALUE_200);

        ScoreRule scoreRuleB = scoreService.addRule(RULE_RAZOAVEL_NAME, RULE_RAZOAVEL_DESC);
        scoreService.addCondition(scoreRuleB, FIELD_TX_VALUE, CONDITION_GREATER_THAN, VALUE_300_00);
        scoreService.addCondition(scoreRuleB, FIELD_TX_VALUE, CONDITION_LESS_THAN, VALUE_5000_00);
        scoreService.addAction(scoreRuleB, FIELD_TX_SCORE, ACTION_ADD, "300"); // Adjusted from VALUE_300 if needed

        ScoreRule scoreRuleC = scoreService.addRule(RULE_ALTA_NAME, RULE_ALTA_DESC);
        scoreService.addCondition(scoreRuleC, FIELD_TX_VALUE, CONDITION_GREATER_THAN_OR_EQUALS, VALUE_5000_00);
        scoreService.addCondition(scoreRuleC, FIELD_TX_VALUE, CONDITION_LESS_THAN, VALUE_20000_00);
        scoreService.addAction(scoreRuleC, FIELD_TX_SCORE, ACTION_ADD, "400"); // Adjusted from VALUE_400 if needed

        ScoreRule scoreRuleD = scoreService.addRule(RULE_SUPER_ALTA_NAME, RULE_SUPER_ALTA_DESC);
        scoreService.addCondition(scoreRuleD, FIELD_TX_VALUE, CONDITION_GREATER_THAN_OR_EQUALS, VALUE_20000);
        scoreService.addAction(scoreRuleD, FIELD_TX_SCORE, ACTION_ADD, VALUE_500);

        ScoreRule scoreRuleE = scoreService.addRule("CPF na lista permissiva", "Regra para CPF na lista permissiva");
        scoreService.addCondition(scoreRuleE, "allowFields", CONDITION_CONTAINS, "cpf");
        scoreService.addAction(scoreRuleE, FIELD_TX_SCORE, ACTION_SUBTRACT, "200");

        ScoreRule scoreRuleF = scoreService.addRule("CPF na lista restritiva", "Regra para CPF na lista restritiva");
        scoreService.addCondition(scoreRuleF, "denyFields", CONDITION_CONTAINS, "cpf");
        scoreService.addAction(scoreRuleF, FIELD_TX_SCORE, ACTION_ADD, "400");

        ScoreRule scoreRuleG = scoreService.addRule("IP na lista restritiva", "Regra para IP na lista restritiva");
        scoreService.addCondition(scoreRuleG, "denyFields", CONDITION_CONTAINS, "ip");
        scoreService.addAction(scoreRuleG, FIELD_TX_SCORE, ACTION_ADD, "400");

        ScoreRule scoreRuleH = scoreService.addRule("DeviceId na lista restritiva", "Regra para DeviceId na lista restritiva");
        scoreService.addCondition(scoreRuleH, "denyFields", CONDITION_CONTAINS, "device_id");
        scoreService.addAction(scoreRuleH, FIELD_TX_SCORE, ACTION_ADD, "400");
    }



    private boolean isConditionMet(ScoreCondition condition, ObjectNode transaction) {
        if (condition == null || condition.getField() == null) {
            return false;
        }
        if (transaction == null || !transaction.has(condition.getField())) {
            return false;
        }
        String originValue = "";
        var fieldValue = transaction.get(condition.getField());
        if(fieldValue instanceof ArrayNode array) {
            originValue = array.valueStream().map(JsonNode::asText)
                    .collect(Collectors.joining(","));
        }else{
            originValue = transaction.get(condition.getField()).textValue();
        }
        String targetValue = condition.getValue();

        switch (condition.getConditionType()) {
            case CONDITION_EQUALS:
                return originValue.equals(targetValue);
            case CONDITION_NOT_EQUALS:
                return !originValue.equals(targetValue);
            case CONDITION_GREATER_THAN_OR_EQUALS:
                return Double.parseDouble(originValue) >= Double.parseDouble(targetValue);
            case CONDITION_GREATER_THAN:
                return Double.parseDouble(originValue) > Double.parseDouble(targetValue);
            case CONDITION_LESS_THAN_OR_EQUALS:
                return Double.parseDouble(originValue) <= Double.parseDouble(targetValue);
            case CONDITION_LESS_THAN:
                return Double.parseDouble(originValue) < Double.parseDouble(targetValue);
            // Add more conditions as needed
            case CONDITION_CONTAINS:
                return originValue.contains(targetValue);
            case CONDITION_NOT_CONTAINS:
                return !originValue.contains(targetValue);
            case CONDITION_BETWEEN:
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

        if (!transaction.has(field)) {
            transaction.put(field, 0);
        }

        switch (actionType) {
            case ACTION_ADD:
                double currentValue = transaction.get(field).asDouble(0);
                double newValue = currentValue + Double.parseDouble(value);
                transaction.put(field, newValue);
                return transaction;
            case ACTION_SUBTRACT:
                double currentValueSub = transaction.get(field).asDouble(0);
                double newValueSub = currentValueSub - Double.parseDouble(value);
                transaction.put(field, newValueSub);
                return transaction;
            // Add more actions as needed
            default:
                return transaction;
        }
    }

    private ObjectNode runActions(ScoreRule rule, ObjectNode transaction) {
        if (rule == null || !rule.isEnabled()) {
            return transaction;
        }
        List<ScoreAction> actions = scoreService.getActionsByRuleId(rule.getId());
        for (ScoreAction action : actions) {
            runAction(action, transaction);
        }
        return transaction;
    }

    public ObjectNode getScore(ObjectNode transaction) {
        List<ScoreCondition> conditions = scoreService.getAllConditions();
        Map<Long, Integer> matchedRulesConditions = new HashMap<>();
        for (ScoreCondition condition : conditions) {
            if (isConditionMet(condition, transaction)) {
                Long ruleId = condition.getRule().getId();
                matchedRulesConditions.put(ruleId, matchedRulesConditions.getOrDefault(ruleId, 0) + 1);
            }
        }
        Map<Long, Integer> allRulesConditionCount = scoreService.getRuleConditionCount(matchedRulesConditions.keySet());
        for (Map.Entry<Long, Integer> entry : matchedRulesConditions.entrySet()) {
            Long ruleId = entry.getKey();
            Integer matchedCount = entry.getValue();
            Integer totalCount = allRulesConditionCount.get(ruleId);
            if (totalCount != null && Objects.equals(matchedCount, totalCount)) {
                System.out.println("Rule " + ruleId + " matched all conditions");
                runActions(scoreService.getRuleById(ruleId), transaction);
            }
        }
        return transaction;
    }
}