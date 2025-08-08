package br.com.acme.motorriskscore.dto;

import br.com.acme.motorriskscore.model.ScoreAction;
import br.com.acme.motorriskscore.model.ScoreCondition;
import br.com.acme.motorriskscore.service.ScoreCalculateService;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.relational.core.sql.In;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;


public class ScoreActionDto {

    private Long id;

    // Dynamic condition type (e.g., temperature threshold, time-based)
    @Pattern(regexp = ScoreCalculateService.FIELD_TX_SCORE,
             message = "Field must be: " + ScoreCalculateService.FIELD_TX_SCORE)
    private String field;

    @NotNull(message = "Action 'actionType' cannot be null")
    @NotEmpty(message = "Action 'actionType' cannot be null or empty")
    @Pattern(regexp = ""+
            ScoreCalculateService.ACTION_ADD+"|" +
            ScoreCalculateService.ACTION_SUBTRACT+"|", message = "'actionType' must be one of: " +
                    ScoreCalculateService.ACTION_ADD + ", " +
                    ScoreCalculateService.ACTION_SUBTRACT
    )
    private String actionType;

    @NotNull(message = "Action 'value' cannot be null")
    @NotEmpty(message = "Action 'value' cannot be null or empty")
    @Pattern(regexp = "\\d+", message = "Action 'value' must be a valid number")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}