package br.com.acme.service;

import br.com.acme.dto.RequestAnaliseRiscoDto;
import br.com.acme.dto.ResponseAnaliseRiscoDto;
import br.com.acme.dto.RequestListasRestricoesDto;
import br.com.acme.dto.ResponseListaRestricoesDto;
import br.com.acme.properties.ConfigScoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnaliseRiscoService {
    private ConfigScoreProperties configScoreProperties;
    private final MotorService motorService;
    private final ListRestricionService listRestricionService;

    final private RestTemplate restTemplate = new RestTemplate();

    public AnaliseRiscoService(ConfigScoreProperties configScoreProperties, MotorService motorService, ListRestricionService listRestricionService) {
        this.configScoreProperties = configScoreProperties;
        this.motorService = motorService;
        this.listRestricionService = listRestricionService;
    }



    public ResponseAnaliseRiscoDto getAnaliseRisco(RequestAnaliseRiscoDto requestAnaliseRiscoDto) {
        final RequestListasRestricoesDto requestListasRestricoesDto = new RequestListasRestricoesDto();
        requestListasRestricoesDto.setCpf(requestAnaliseRiscoDto.getCpf());
        requestListasRestricoesDto.setDeviceId(requestAnaliseRiscoDto.getDeviceId());
        requestListasRestricoesDto.setIp(requestAnaliseRiscoDto.getIp());

        var response = listRestricionService.postListRestrictions(requestListasRestricoesDto);

        if(response == null) {
            return new ResponseAnaliseRiscoDto();
        }

        requestAnaliseRiscoDto.setAllowedFields(response.getAllowFields());
        requestAnaliseRiscoDto.setDeniedFields(response.getDeniedFields());

        var respostaMotor = motorService.postMotorRisk(requestAnaliseRiscoDto);
        for(ConfigScoreProperties.ScoreRangeDefinition range: configScoreProperties.getRanges()){
            if(respostaMotor == null || !respostaMotor.has("tx_score")) {
                return new ResponseAnaliseRiscoDto("INVALID_RESPONSE");
            }
            var score = respostaMotor.get("tx_score").asInt(0);
            if(score >= range.getStart() && score <= range.getEnd()) {
                return new ResponseAnaliseRiscoDto(range.getStatus());
            }
        }
        return new ResponseAnaliseRiscoDto();

    }
}
