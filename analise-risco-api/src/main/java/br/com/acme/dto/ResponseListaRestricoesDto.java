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

    public List<String> denyFields;

    public List<String> allowFields;

    public List<String> getDenyFields() {
        return denyFields;
    }

    public void setDenyFields(List<String> denyFields) {
        this.denyFields = denyFields;
    }

    public List<String> getAllowFields() {
        return allowFields;
    }

    public void setAllowFields(List<String> allowFields) {
        this.allowFields = allowFields;
    }

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
}
