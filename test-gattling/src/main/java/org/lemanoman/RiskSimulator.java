package org.lemanoman;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.http.HttpDsl.http;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import com.github.javafaker.Faker;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.OpenInjectionStep.RampRate.RampRateOpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;


public class RiskSimulator extends Simulation {


    public RiskSimulator() {

        setUp(buildPostScenario()
                .injectOpen(injection())
                .protocols(setupProtocol())).assertions(global().responseTime()
                .max()
                .lte(10000), global().successfulRequests()
                .percent()
                .gt(90d));
    }
    public static Iterator<Map<String,Object>> deviceFeedData() {
        Faker faker = new Faker();
        Iterator<Map<String, Object>> iterator;
        iterator = Stream.generate(() -> {
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("ip", faker.internet()
                    .ipV4Address());
            stringObjectMap.put("cpf", "422.222.222-22");
            stringObjectMap.put("deviceId", faker.number()
                    .digits(10));
            stringObjectMap.put("txType", "PIX");
            stringObjectMap.put("value", faker.number()
                    .numberBetween(10, 5000));
            return stringObjectMap;
        })
          .iterator();
        return iterator;
    }

    private static ScenarioBuilder buildPostScenario() {
        return CoreDsl.scenario("Load POST Test")
                .feed(deviceFeedData())
                .exec(http("create-book").post("/api/analise_risco/")
                        .header("Content-Type", "application/json")
                        .body(StringBody("{\n" +
                                "  \"ip\": \"${ip}\",\n" +
                                "  \"cpf\": \"${cpf}\",\n" +
                                "  \"device_id\": \"${deviceId}\",\n" +
                                "  \"tx_type\": \"${txType}\",\n" +
                                "  \"tx_value\": \"${value}\"\n" +
                                "}")));
    }

    private static Iterator <Map<String, Object>> feedData() {
        Faker faker = new Faker();
        Iterator<Map<String, Object>> iterator;
        iterator = Stream.generate(() -> {
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("title", faker.book()
                    .title());
            return stringObjectMap;
        })
          .iterator();
        return iterator;
    }

    private static HttpProtocolBuilder setupProtocol() {
        return HttpDsl.http.baseUrl("http://localhost:8190")
                .acceptHeader("application/json")
                .maxConnectionsPerHost(10)
                .userAgentHeader("Performance Test");
    }

    private RampRateOpenInjectionStep injection() {
        int totalUsers = 100;
        double userRampUpPerInterval = 10;
        double rampUpIntervalInSeconds = 300;

        int rampUptimeSeconds = 1;
        int duration = 300;
        return rampUsersPerSec(userRampUpPerInterval / (rampUpIntervalInSeconds)).to(totalUsers)
                .during(Duration.ofSeconds(rampUptimeSeconds + duration));
    }
}
