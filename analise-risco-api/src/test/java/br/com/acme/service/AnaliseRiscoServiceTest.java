package br.com.acme.service;

import br.com.acme.dto.RequestAnaliseRiscoDto;
import br.com.acme.dto.ResponseAnaliseRiscoDto;
import br.com.acme.dto.RequestListasRestricoesDto;
import br.com.acme.dto.ResponseListaRestricoesDto;
import br.com.acme.properties.ConfigScoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AnaliseRiscoServiceTest {

    @Mock
    private MotorService motorService;

    @Mock
    private ListRestricionService listRestricionService;

    @Mock
    private ConfigScoreProperties configScoreProperties;

    @InjectMocks
    private AnaliseRiscoService analiseRiscoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        configScoreProperties = new ConfigScoreProperties();
        List<ConfigScoreProperties.ScoreRangeDefinition> ranges = new ArrayList<>();

        ConfigScoreProperties.ScoreRangeDefinition rangeAprovado = new ConfigScoreProperties.ScoreRangeDefinition();
        rangeAprovado.setStart(300);
        rangeAprovado.setEnd(10000);
        rangeAprovado.setStatus("APROVADO");

        ConfigScoreProperties.ScoreRangeDefinition rangeNegado = new ConfigScoreProperties.ScoreRangeDefinition();
        rangeNegado.setStart(0);
        rangeNegado.setEnd(299);
        rangeNegado.setStatus("NEGADO");

        ranges.add(rangeAprovado);
        ranges.add(rangeNegado);
        configScoreProperties.setRanges(ranges);

        analiseRiscoService = new AnaliseRiscoService(configScoreProperties, motorService, listRestricionService);
    }

    @Test
    public void testListRestricionServiceReturnsNull() {
        RequestAnaliseRiscoDto request = new RequestAnaliseRiscoDto();
        Mockito.when(listRestricionService.postListRestrictions(Mockito.any(RequestListasRestricoesDto.class)))
                .thenReturn(null);

        ResponseAnaliseRiscoDto response = analiseRiscoService.getAnaliseRisco(request);
        // If list restrictions are null, a new default ResponseAnaliseRiscoDto is returned (status is not set)
        assertEquals("ERROR", response.getTxDecision());
    }

    @Test
    public void testMotorServiceReturnsNull() {
        RequestAnaliseRiscoDto request = new RequestAnaliseRiscoDto();
        ResponseListaRestricoesDto dummyResponse = new ResponseListaRestricoesDto();
        dummyResponse.setAllowFields(List.of("dummy"));
        dummyResponse.setDeniedFields(List.of("dummy"));

        Mockito.when(listRestricionService.postListRestrictions(Mockito.any(RequestListasRestricoesDto.class)))
                .thenReturn(dummyResponse);
        Mockito.when(motorService.postMotorRisk(Mockito.any(RequestAnaliseRiscoDto.class)))
                .thenReturn(null);

        ResponseAnaliseRiscoDto response = analiseRiscoService.getAnaliseRisco(request);
        assertEquals("INVALID_RESPONSE", response.getTxDecision());
    }

    @Test
    public void testMotorServiceWithoutTxScore() {
        RequestAnaliseRiscoDto request = new RequestAnaliseRiscoDto();
        ResponseListaRestricoesDto dummyResponse = new ResponseListaRestricoesDto();
        dummyResponse.setAllowFields(List.of("dummy"));
        dummyResponse.setDeniedFields(List.of("dummy"));

        Mockito.when(listRestricionService.postListRestrictions(Mockito.any(RequestListasRestricoesDto.class)))
                .thenReturn(dummyResponse);

        // Create an ObjectNode without tx_score field
        ObjectNode node = objectMapper.createObjectNode();
        Mockito.when(motorService.postMotorRisk(Mockito.any(RequestAnaliseRiscoDto.class)))
                .thenReturn(node);

        ResponseAnaliseRiscoDto response = analiseRiscoService.getAnaliseRisco(request);
        assertEquals("INVALID_RESPONSE", response.getTxDecision());
    }

    @Test
    public void testScoreInAprovadoRange() {
        RequestAnaliseRiscoDto request = new RequestAnaliseRiscoDto();
        ResponseListaRestricoesDto dummyResponse = new ResponseListaRestricoesDto();
        dummyResponse.setAllowFields(List.of("dummy"));
        dummyResponse.setDeniedFields(List.of("dummy"));

        Mockito.when(listRestricionService.postListRestrictions(Mockito.any(RequestListasRestricoesDto.class)))
                .thenReturn(dummyResponse);

        // Create an ObjectNode with tx_score = 500; falls in [300, 10000] which returns "APROVADO"
        ObjectNode node = objectMapper.createObjectNode();
        node.put("tx_score", 500);
        Mockito.when(motorService.postMotorRisk(Mockito.any(RequestAnaliseRiscoDto.class)))
                .thenReturn(node);

        ResponseAnaliseRiscoDto response = analiseRiscoService.getAnaliseRisco(request);
        assertEquals("APROVADO", response.getTxDecision());
    }

    @Test
    public void testScoreInNegadoRange() {
        RequestAnaliseRiscoDto request = new RequestAnaliseRiscoDto();
        ResponseListaRestricoesDto dummyResponse = new ResponseListaRestricoesDto();
        dummyResponse.setAllowFields(List.of("dummy"));
        dummyResponse.setDeniedFields(List.of("dummy"));

        Mockito.when(listRestricionService.postListRestrictions(Mockito.any(RequestListasRestricoesDto.class)))
                .thenReturn(dummyResponse);

        // Create an ObjectNode with tx_score = 100; falls in [0, 299] which returns "NEGADO"
        ObjectNode node = objectMapper.createObjectNode();
        node.put("tx_score", 100);
        Mockito.when(motorService.postMotorRisk(Mockito.any(RequestAnaliseRiscoDto.class)))
                .thenReturn(node);

        ResponseAnaliseRiscoDto response = analiseRiscoService.getAnaliseRisco(request);
        assertEquals("NEGADO", response.getTxDecision());
    }
}