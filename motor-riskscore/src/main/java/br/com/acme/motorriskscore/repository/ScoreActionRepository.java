package br.com.acme.motorriskscore.repository;

import br.com.acme.motorriskscore.model.ScoreAction;
import br.com.acme.motorriskscore.model.ScoreRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScoreActionRepository extends JpaRepository<ScoreAction, Long> {
    List<ScoreAction> searchAllByRuleId(Long ruleId);
}
