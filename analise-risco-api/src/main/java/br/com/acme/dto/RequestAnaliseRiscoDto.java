package br.com.acme.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RequestAnaliseRiscoDto {
    private String ip;
    private String cpf;
    @JsonProperty("device_id")
    @JsonAlias("deviceId")
    private String deviceId;

    @JsonProperty("tx_type")
    @JsonAlias("txType")
    private String txType;

    @JsonProperty("tx_value")
    @JsonAlias("txValue")
    private String txValue;

    @JsonProperty("denied_fields")
    @JsonAlias("deniedFields")
    private List<String> deniedFields;

    @JsonProperty("allowed_fields")
    @JsonAlias("allowedFields")
    private List<String> allowedFields;

    public List<String> getDeniedFields() {
        return deniedFields;
    }

    public void setDeniedFields(List<String> deniedFields) {
        this.deniedFields = deniedFields;
    }

    public List<String> getAllowedFields() {
        return allowedFields;
    }

    public void setAllowedFields(List<String> allowedFields) {
        this.allowedFields = allowedFields;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public String getTxValue() {
        return txValue;
    }

    public void setTxValue(String txValue) {
        this.txValue = txValue;
    }
}
