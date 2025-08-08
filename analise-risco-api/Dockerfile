FROM maven:3-amazoncorretto-21 AS BUILD
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:21
COPY --from=BUILD /app/target/*.jar /app/analise-risco-api.jar

ENTRYPOINT ["java", "-jar" , "/app/analise-risco-api.jar"]