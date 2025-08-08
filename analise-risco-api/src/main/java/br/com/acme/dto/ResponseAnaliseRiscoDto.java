package br.com.acme.dto;

public class ResponseAnaliseRiscoDto {
    private String txDecision = "ERROR";

    public String getTxDecision() {
        return txDecision;
    }

    public void setTxDecision(String txDecision) {
        this.txDecision = txDecision;
    }
}
