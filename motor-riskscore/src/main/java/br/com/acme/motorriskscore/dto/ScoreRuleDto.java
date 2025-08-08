package br.com.acme.motorriskscore.dto;

import br.com.acme.motorriskscore.model.ScoreAction;
import br.com.acme.motorriskscore.model.ScoreCondition;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;


public class ScoreRuleDto {
    private Long id;

    @NotEmpty(message = "Name cannot be null or empty")
    @NotNull(message = "Name cannot be null or empty")
    @Size(max = 100, message = "Name must be at most 100 characters long")
    private String name;

    @Size(max = 255, message = "Description must be at most 255 characters long")
    private String description;
    
    private boolean enabled = true;

    @NotEmpty(
            message = "At least one condition is required"
    )
    @NotNull(
            message = "Conditions cannot be null"
    )
    @Valid
    private List<ScoreConditionDto> conditions;

    @NotEmpty(
            message = "At least one action is required"
    )
    @NotNull(
            message = "Actions cannot be null"
    )
    @Valid
    private List<ScoreActionDto> actions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<ScoreConditionDto> getConditions() {
        return conditions;
    }

    public void setConditions(List<ScoreConditionDto> conditions) {
        this.conditions = conditions;
    }

    public List<ScoreActionDto> getActions() {
        return actions;
    }

    public void setActions(List<ScoreActionDto> actions) {
        this.actions = actions;
    }
}