// File: src/test/java/br/com/acme/listaspermrestrapi/RuleControllerTest.java
package br.com.acme.listaspermrestrapi;

import br.com.acme.listaspermrestrapi.controller.RuleController;
import br.com.acme.listaspermrestrapi.dto.RuleDto;
import br.com.acme.listaspermrestrapi.model.RuleModel;
import br.com.acme.listaspermrestrapi.service.RuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RuleController.class)
class RuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RuleService ruleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddAllowRule() throws Exception {
        // Create a sample DTO. Adjust setter methods based on your actual RuleDto.
        RuleDto ruleDto = new RuleDto();
        ruleDto.setCpf("12345678900");

        RuleModel rule = new RuleModel();
        rule.setRuleId(UUID.randomUUID());
        rule.setRuleFieldName("cpf");
        rule.setRuleFieldValue("12345678900");
        rule.setRuleAllow(true);

        List<RuleModel> ruleList = List.of(rule);

        when(ruleService.addRule(any(RuleDto.class), any(Boolean.class))).thenReturn(ruleList);

        mockMvc.perform(post("/api/rule/allow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].ruleId").value("1"))
                .andExpect(jsonPath("$[0].ruleFieldName").value("cpf"))
                .andExpect(jsonPath("$[0].ruleFieldValue").value("12345678900"))
                .andExpect(jsonPath("$[0].ruleAllow").value(true));
    }

    @Test
    void testAddDenyRule() throws Exception {
        RuleDto ruleDto = new RuleDto();
        ruleDto.setCpf("98765432100");

        RuleModel rule = new RuleModel();
        rule.setRuleId(UUID.randomUUID());
        rule.setRuleFieldName("cpf");
        rule.setRuleFieldValue("98765432100");
        rule.setRuleAllow(false);

        List<RuleModel> ruleList = List.of(rule);

        when(ruleService.addRule(any(RuleDto.class), any(Boolean.class))).thenReturn(ruleList);

        mockMvc.perform(post("/api/rule/deny")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].ruleId").value("2"))
                .andExpect(jsonPath("$[0].ruleFieldName").value("cpf"))
                .andExpect(jsonPath("$[0].ruleFieldValue").value("98765432100"))
                .andExpect(jsonPath("$[0].ruleAllow").value(false));
    }

    @Test
    void testGetAllRules() throws Exception {
        RuleModel rule1 = new RuleModel();
        rule1.setRuleId(UUID.randomUUID());
        rule1.setRuleFieldName("cpf");
        rule1.setRuleFieldValue("12345678900");
        rule1.setRuleAllow(true);

        RuleModel rule2 = new RuleModel();
        rule2.setRuleId(UUID.randomUUID());
        rule2.setRuleFieldName("ip");
        rule2.setRuleFieldValue("192.168.1.1");
        rule2.setRuleAllow(false);

        List<RuleModel> ruleList = Arrays.asList(rule1, rule2);

        when(ruleService.getListAll()).thenReturn(ruleList);

        mockMvc.perform(get("/api/rule/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ruleId").value("1"))
                .andExpect(jsonPath("$[0].ruleFieldName").value("cpf"))
                .andExpect(jsonPath("$[0].ruleFieldValue").value("12345678900"))
                .andExpect(jsonPath("$[0].ruleAllow").value(true))
                .andExpect(jsonPath("$[1].ruleId").value("2"))
                .andExpect(jsonPath("$[1].ruleFieldName").value("ip"))
                .andExpect(jsonPath("$[1].ruleFieldValue").value("192.168.1.1"))
                .andExpect(jsonPath("$[1].ruleAllow").value(false));
    }


}