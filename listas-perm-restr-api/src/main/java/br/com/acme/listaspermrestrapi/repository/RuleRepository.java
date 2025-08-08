package br.com.acme.listaspermrestrapi.repository;

import br.com.acme.listaspermrestrapi.model.RuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<RuleModel, Long> {
    List<RuleModel> searchAllByRuleFieldNameAndRuleFieldValue(String resFieldName, String resFieldValue);
    RuleModel findByRuleFieldNameAndRuleFieldValueAndRuleAllow(String resFieldName, String resFieldValue, Boolean resAllow);

}
