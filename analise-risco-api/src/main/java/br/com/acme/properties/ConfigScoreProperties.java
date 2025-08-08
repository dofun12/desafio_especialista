package br.com.acme.properties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;


@ConfigurationProperties(prefix = "score")
@RefreshScope
@Validated
public class ConfigScoreProperties {

    private List<ScoreRangeDefinition> ranges = new ArrayList<>();

    public static class ScoreRangeDefinition {
        @NotNull
        @Range(min = 0, max = Integer.MAX_VALUE)
        private Integer start;

        @NotNull
        @Range(min = 0, max = Integer.MAX_VALUE)
        private Integer end;

        @NotBlank
        private String status;

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public List<ScoreRangeDefinition> getRanges() {
        return ranges;
    }

    public void setRanges(List<ScoreRangeDefinition> ranges) {
        this.ranges = ranges;
    }
}
