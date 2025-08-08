package br.com.acme.motorriskscore.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

public class RequestAnaliseRiscoDto {
    private static final String IPV4_REGEX =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    @Pattern(regexp = IPV4_REGEX, message = "Invalid IP address format")
    private String ip;

    @Pattern(regexp = "(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)", message = "Invalid CPF format")
    private String cpf;

    @JsonProperty("device_id")
    @JsonAlias("deviceId")
    @Size(max = 255, message = "Device ID must be at most 255 characters long")
    private String deviceId;

    @JsonProperty("tx_type")
    @JsonAlias("txType")
    @NotEmpty(message = "Transaction type cannot be empty")
    @NotNull(message = "Transaction type cannot be null")
    @Size(min = 1, max = 50, message = "Transaction type must be between 1 and 50 characters long")
    private String txType;

    @JsonProperty("tx_value")
    @JsonAlias("txValue")
    @DecimalMin(value = "0.01", inclusive = true, message = "Transaction value must be at least 0.01")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @NotEmpty(message = "Transaction value cannot be empty")
    @NotNull(message = "Transaction value cannot be null")
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
