package br.com.acme.motorriskscore.repository;

import br.com.acme.motorriskscore.model.ScoreCondition;
import br.com.acme.motorriskscore.model.ScoreRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ScoreConditionRepository extends JpaRepository<ScoreCondition, Long> {
}
