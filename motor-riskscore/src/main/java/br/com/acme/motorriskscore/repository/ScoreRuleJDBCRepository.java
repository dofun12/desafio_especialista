package br.com.acme.motorriskscore.repository;

import br.com.acme.motorriskscore.dto.RuleCountConditionsDto;
import br.com.acme.motorriskscore.model.ScoreRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ScoreRuleJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ScoreRuleJDBCRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public Map<Long, Integer> getRuleConditionCount(Set<Long> rulesMatched) {
        String sql = "SELECT r.id, COUNT(c.id) AS condition_count " +
                     "FROM score_rule r " +
                     "INNER JOIN score_condition c ON r.id = c.rule_id " +
                     "WHERE r.enabled = true and r.id in (:rulesMatched)" +
                     "GROUP BY r.id";
        SqlParameterSource parameters = new MapSqlParameterSource("rulesMatched", new ArrayList<>(rulesMatched));

        return namedParameterJdbcTemplate.queryForList(sql,parameters).stream().collect(Collectors.toMap(stringObjectMap -> ((Number) stringObjectMap.get("id")).longValue(), stringObjectMap -> ((Number) stringObjectMap.get("condition_count")).intValue()));
    }

}
