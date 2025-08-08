package br.com.acme.service;

import br.com.acme.dto.RequestAnaliseRiscoDto;
import br.com.acme.dto.RequestListasRestricoesDto;
import br.com.acme.dto.ResponseListaRestricoesDto;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ListRestricionService {
    @Value("${list-perm-restr.url}")
    private String listPermRestrUrl;

    final RestTemplate restTemplate = new RestTemplate();


    @Retry(name = "retryListaRestricao", fallbackMethod = "postMotorRiskFallback")
    public ResponseListaRestricoesDto postListRestrictions(RequestListasRestricoesDto listasRestricoesDto) {
        return restTemplate.postForObject(listPermRestrUrl+"/api/rule/validate", listasRestricoesDto, ResponseListaRestricoesDto.class);
    }


    public ResponseListaRestricoesDto postMotorRiskFallback(RequestListasRestricoesDto listasRestricoesDto, Throwable throwable) {
        // Log the error or handle it as needed
        System.err.println("Error calling motor risk score service: " + throwable.getMessage());
        return null; // or return a default ObjectNode if needed
    }
}
