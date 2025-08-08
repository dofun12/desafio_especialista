package br.com.acme.listaspermrestrapi.model;

import jakarta.persistence.*;

@Table(name = "rule", indexes = @Index(columnList = "ruleFieldName,ruleFieldValue"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"ruleFieldName","ruleFieldValue", "ruleAllow"}))
@Entity
public class RuleModel {
    /**
     * Adicionando prefixo nos campos para evitar conflitos com nomes reservados do banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(columnDefinition = "BINARY(16)") // For MySQL
    @Column
    private Long ruleId;

    @Column(nullable = false)
    private String ruleFieldName;

    @Column(nullable = false)
    private String ruleFieldValue;

    @Column(nullable = false)
    private Boolean ruleAllow = false;

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleFieldName() {
        return ruleFieldName;
    }

    public void setRuleFieldName(String ruleFieldName) {
        this.ruleFieldName = ruleFieldName;
    }

    public String getRuleFieldValue() {
        return ruleFieldValue;
    }

    public void setRuleFieldValue(String ruleFieldValue) {
        this.ruleFieldValue = ruleFieldValue;
    }

    public Boolean getRuleAllow() {
        return ruleAllow;
    }

    public void setRuleAllow(Boolean ruleAllow) {
        this.ruleAllow = ruleAllow;
    }
}
