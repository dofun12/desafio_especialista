package br.com.acme.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseListaRestricoesDto {
    private String cpf;
    private String ip;

    @JsonAlias("deviceId")
    @JsonProperty("device_id")
    private String deviceId;

    public List<String> deniedFields;

    public List<String> allowFields;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<String> getDeniedFields() {
        return deniedFields;
    }

    public void setDeniedFields(List<String> deniedFields) {
        this.deniedFields = deniedFields;
    }

    public List<String> getAllowFields() {
        return allowFields;
    }

    public void setAllowFields(List<String> allowFields) {
        this.allowFields = allowFields;
    }
}
