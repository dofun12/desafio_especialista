package br.com.acme.motorriskscore.repository;

import br.com.acme.motorriskscore.model.ScoreRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface ScoreRuleRepository extends JpaRepository<ScoreRule, Long> {

}
