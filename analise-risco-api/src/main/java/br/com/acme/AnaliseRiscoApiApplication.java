package br.com.acme;

import br.com.acme.properties.ConfigScoreProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigScoreProperties.class)
public class AnaliseRiscoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnaliseRiscoApiApplication.class, args);
    }

}
