package br.com.acme.motorriskscore.dto;

import br.com.acme.motorriskscore.model.ScoreAction;
import br.com.acme.motorriskscore.model.ScoreCondition;
import br.com.acme.motorriskscore.model.ScoreRule;
import br.com.acme.motorriskscore.service.ScoreCalculateService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;


public class ScoreConditionDto {
    private Long id;

    @Pattern(regexp = ScoreCalculateService.FIELD_TX_TYPE+"|"+ScoreCalculateService.FIELD_TX_VALUE+"|"+ScoreCalculateService.FIELD_TX_SCORE+"|cpf|device_id|ip",
            message = "Condition 'field' must be one of: " +
                    ScoreCalculateService.FIELD_TX_TYPE + ", " +
                    ScoreCalculateService.FIELD_TX_VALUE + ", " +
                    ScoreCalculateService.FIELD_TX_SCORE + ", " +
                    "cpf, device_id, ip")
    @NotNull(message = "Condition 'field' cannot be null or empty")
    @NotEmpty(message = "Condition 'field' cannot be null or empty")
    private String field;

    @NotNull(message = "Condition 'conditionType' cannot be null or empty")
    @NotEmpty(message = "Condition 'conditionType' cannot be null or empty")
    @Pattern(regexp = ""+
            ScoreCalculateService.CONDITION_BETWEEN+"|"+
            ScoreCalculateService.CONDITION_GREATER_THAN+ "|" +
            ScoreCalculateService.CONDITION_LESS_THAN + "|" +
            ScoreCalculateService.CONDITION_BETWEEN + "|" +
            ScoreCalculateService.CONDITION_CONTAINS + "|" +
            ScoreCalculateService.CONDITION_EQUALS + "|" +
            ScoreCalculateService.CONDITION_NOT_EQUALS + "|" +
            ScoreCalculateService.CONDITION_GREATER_THAN_OR_EQUALS + "|" +
            ScoreCalculateService.CONDITION_LESS_THAN_OR_EQUALS,
             message = "'conditionType' must be one of: " +
                     ScoreCalculateService.CONDITION_BETWEEN + ", " +
                     ScoreCalculateService.CONDITION_GREATER_THAN + ", " +
                     ScoreCalculateService.CONDITION_LESS_THAN + ", " +
                     ScoreCalculateService.CONDITION_CONTAINS + ", " +
                     ScoreCalculateService.CONDITION_EQUALS + ", " +
                     ScoreCalculateService.CONDITION_NOT_EQUALS + ", " +
                     ScoreCalculateService.CONDITION_GREATER_THAN_OR_EQUALS + ", " +
                     ScoreCalculateService.CONDITION_LESS_THAN_OR_EQUALS)
    private String conditionType;

    @NotEmpty(message = "Condition 'value' cannot be null or empty")
    @NotNull(message = "Condition 'value' cannot be null or empty")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}