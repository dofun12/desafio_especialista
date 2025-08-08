package br.com.acme.service;

import br.com.acme.dto.RequestAnaliseRiscoDto;
import br.com.acme.dto.ResponseAnaliseRiscoDto;
import br.com.acme.dto.RequestListasRestricoesDto;
import br.com.acme.dto.ResponseListaRestricoesDto;
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

        var respostaMotor = restTemplate.postForObject(motorRiskScoreUrl+"")


        return new ResponseAnaliseRiscoDto();

    }
}
