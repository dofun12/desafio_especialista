package br.com.acme.motorriskscore.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ScoreRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String description;
    
    private boolean enabled;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScoreCondition> conditions;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScoreAction> actions;

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

    public List<ScoreCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<ScoreCondition> conditions) {
        this.conditions = conditions;
    }

    public List<ScoreAction> getActions() {
        return actions;
    }

    public void setActions(List<ScoreAction> actions) {
        this.actions = actions;
    }
}