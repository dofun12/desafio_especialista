package br.com.acme.dto;

public class ResponseAnaliseRiscoDto {
    private String txDecision = "ERROR";

    public ResponseAnaliseRiscoDto() {
    }

    public ResponseAnaliseRiscoDto(String txDecision) {
        this.txDecision = txDecision;
    }

    public String getTxDecision() {
        return txDecision;
    }

    public void setTxDecision(String txDecision) {
        this.txDecision = txDecision;
    }
}
