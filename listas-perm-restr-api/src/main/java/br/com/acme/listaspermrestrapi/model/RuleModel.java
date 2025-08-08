package br.com.acme.listaspermrestrapi.model;

import jakarta.persistence.*;

import java.util.UUID;

@Table(indexes = @Index(columnList = "ruleFieldName,ruleFieldValue"))
@Entity
public class RuleModel {
    /**
     * Adicionando prefixo nos campos para evitar conflitos com nomes reservados do banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)") // For MySQL
    // @Column(columnDefinition = "BYTEA") // For PostgreSQL
    private UUID ruleId;

    @Column(nullable = false)
    private String ruleFieldName;

    @Column(nullable = false)
    private String ruleFieldValue;

    @Column(nullable = false)
    private Boolean ruleAllow = false;

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
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
