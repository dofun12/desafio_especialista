package br.com.acme.service;

import br.com.acme.dto.RequestAnaliseRiscoDto;
import br.com.acme.dto.ResponseAnaliseRiscoDto;
import br.com.acme.dto.RequestListasRestricoesDto;
import br.com.acme.dto.ResponseListaRestricoesDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnaliseRiscoService {

    @Value("${list-perm-restr.url}")
    private String listPermRestrUrl;

    @Value("${motor-riskscore.url}")
    private String motorRiskScoreUrl;

    final private RestTemplate restTemplate = new RestTemplate();
    public AnaliseRiscoService() {
    }


    public ResponseAnaliseRiscoDto getAnaliseRisco(RequestAnaliseRiscoDto requestAnaliseRiscoDto) {
        final RequestListasRestricoesDto requestListasRestricoesDto = new RequestListasRestricoesDto();
        requestListasRestricoesDto.setCpf(requestAnaliseRiscoDto.getCpf());
        requestListasRestricoesDto.setDeviceId(requestAnaliseRiscoDto.getDeviceId());
        requestListasRestricoesDto.setIp(requestAnaliseRiscoDto.getIp());

        var response = restTemplate.postForObject(listPermRestrUrl+"/api/rule/validate", requestListasRestricoesDto, ResponseListaRestricoesDto.class);
        if(response == null) {
            return new ResponseAnaliseRiscoDto();
        }
        requestAnaliseRiscoDto.setAllowedFields(response.getAllowFields());
        requestAnaliseRiscoDto.setDeniedFields(response.getDeniedFields());
        try {
            System.out.println(new ObjectMapper().writeValueAsString(requestAnaliseRiscoDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        var respostaMotor = restTemplate.postForObject(motorRiskScoreUrl+"/api/score/calculate", requestAnaliseRiscoDto, ObjectNode.class);
        return new ResponseAnaliseRiscoDto();

    }
}
