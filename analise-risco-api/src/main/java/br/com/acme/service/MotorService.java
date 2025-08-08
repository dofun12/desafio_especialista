package br.com.acme.service;

import br.com.acme.dto.RequestAnaliseRiscoDto;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MotorService {
    @Value("${motor-riskscore.url}")
    private String motorRiskScoreUrl;



    @Retry(name = "retryMotorRisk", fallbackMethod = "postMotorRiskFallback")
    public ObjectNode postMotorRisk(RequestAnaliseRiscoDto requestAnaliseRiscoDto) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(motorRiskScoreUrl+"/api/score/calculate", requestAnaliseRiscoDto, ObjectNode.class);
    }


    public ObjectNode postMotorRiskFallback(RequestAnaliseRiscoDto requestAnaliseRiscoDto, Throwable throwable) {
        // Log the error or handle it as needed
        System.err.println("Error calling motor risk score service: " + throwable.getMessage());
        return null; // or return a default ObjectNode if needed
    }
}
