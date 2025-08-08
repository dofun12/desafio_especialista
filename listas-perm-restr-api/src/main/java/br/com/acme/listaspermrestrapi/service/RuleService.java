package br.com.acme.listaspermrestrapi.service;

import br.com.acme.listaspermrestrapi.dto.RuleDto;
import br.com.acme.listaspermrestrapi.dto.VerifyRuleDto;
import br.com.acme.listaspermrestrapi.model.RuleModel;
import br.com.acme.listaspermrestrapi.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {
    static final String CPF_FIELD = "cpf";
    static final String IP_FIELD = "ip";
    static final String DEVICE_ID_FIELD = "device_id";
    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;

        addRule(CPF_FIELD, "123456", true);
        addRule(IP_FIELD, "127.0.0.1", false);
        addRule(DEVICE_ID_FIELD, "device123", true);
    }

    private RuleModel addRule(String fieldName, String fieldValue, boolean allow) {
        final RuleModel ruleModel = new RuleModel();
        ruleModel.setRuleFieldName(fieldName);
        ruleModel.setRuleFieldValue(fieldValue);
        ruleModel.setRuleAllow(allow);
        return ruleRepository.save(ruleModel);
    }

    public List<RuleModel> addRule(RuleDto ruleDto, boolean allow){

        List<RuleModel> savedRestrictions = new ArrayList<>();
        if (ruleDto.getCpf() != null) {
            savedRestrictions.add(addRule(CPF_FIELD, ruleDto.getCpf(), allow));
        }
        if (ruleDto.getIp() != null) {
            savedRestrictions.add(addRule(IP_FIELD, ruleDto.getIp(), allow));
        }
        if (ruleDto.getDeviceId() != null) {
            savedRestrictions.add(addRule(DEVICE_ID_FIELD, ruleDto.getDeviceId(), allow));
        }
        return savedRestrictions;
    }

    public RuleModel findById(String id) {
        return ruleRepository.findById(id).orElse(null);
    }

    public RuleModel save(RuleModel restrictionModel) {
        return ruleRepository.save(restrictionModel);
    }

    public VerifyRuleDto verifyRule(RuleDto ruleDto) {
        VerifyRuleDto verifyRuleDto = new VerifyRuleDto();
        List<String> allowedFields = new ArrayList<>();
        List<String> deniedFields = new ArrayList<>();

        searchByFieldAndValue(CPF_FIELD, ruleDto.getCpf())
            .forEach(rule -> {
                if(Boolean.TRUE.equals(rule.getRuleAllow())) {
                    allowedFields.add(CPF_FIELD);
                } else {
                    deniedFields.add(CPF_FIELD);
                }
        });
        searchByFieldAndValue(IP_FIELD, ruleDto.getIp())
            .forEach(rule -> {
                if(Boolean.TRUE.equals(rule.getRuleAllow())) {
                    allowedFields.add(IP_FIELD);
                } else {
                    deniedFields.add(IP_FIELD);
                }
        });
        searchByFieldAndValue(DEVICE_ID_FIELD, ruleDto.getDeviceId())
            .forEach(rule -> {
                if(Boolean.TRUE.equals(rule.getRuleAllow())) {
                    allowedFields.add(DEVICE_ID_FIELD);
                } else {
                    deniedFields.add(DEVICE_ID_FIELD);
                }
        });
        verifyRuleDto.setAllowFields(allowedFields);
        verifyRuleDto.setDenyFields(deniedFields);
        verifyRuleDto.setCpf(ruleDto.getCpf());
        verifyRuleDto.setIp(ruleDto.getIp());
        verifyRuleDto.setDeviceId(ruleDto.getDeviceId());
        return verifyRuleDto;
    }

    public List<RuleModel> searchByFieldAndValue(String field, String value) {
        return ruleRepository.searchAllByRuleFieldNameAndRuleFieldValue(field, value);
    }

    public List<RuleModel> getListAll() {
        return ruleRepository.findAll();
    }

    public void delete(String id) {
        ruleRepository.deleteById(id);
    }
}
