package br.com.acme.motorriskscore.model;

import jakarta.persistence.*;

@Entity
public class ScoreCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dynamic condition type (e.g., temperature threshold, time-based)
    private String field;

    // Parameters stored in JSON or key-value string
    private String conditionType;

    private String value;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private ScoreRule rule;


    public ScoreCondition(ScoreRule rule, String field, String conditionType, String value) {
        this.rule = rule;
        this.field = field;
        this.conditionType = conditionType;
        this.value = value;
    }

    public ScoreCondition() {
    }

    // Getters and setters


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

    public ScoreRule getRule() {
        return rule;
    }

    public void setRule(ScoreRule rule) {
        this.rule = rule;
    }
}