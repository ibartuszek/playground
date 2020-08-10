# Playground:
The purpose of this project is to try-out different technologies and frameworks with java-based
spring-boot web-application. They are separated into different packages and different commits.
 - spring web-flux (webclient)
 - open-api 3 (openapi)

### Sample Usage
User can use the application via:
<a href=http://localhost:8080/swagger-ui.html#/>localhost:8080/swagger-ui.html</a>

### Dependencies:
 - Spring-boot
 - org.springdoc:springdoc-openapi-data-rest
 - org.springdoc:springdoc-openapi-ui
 - org.springframework.boot:spring-boot-starter-webflux
 - org.springframework.kafka:spring-kafka

## Build:
 - clone from github: `git clone git@github.com:ibartuszek/playground.git`
 - `./mvnw clean package` at the main folder of the project

## Run:
 - `./mvnw spring-boot:run` at the main folder of the project to start application. This case you can reach swagger on: <a href=http://localhost:8080/swagger-ui.html#/>localhost:8080</a> port.

### Useful links
- <a href="https://github.com/ibartuszek/playground">playground github repo</a>
- <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/">Spring boot documentation</a>
- <a href="https://springdoc.org">springdoc</a>
- <a href="https://kafka.apache.org/">kafka</a>
- <a href="https://www.baeldung.com/spring-kafka">Baeldung: Intro to Apache Kafka with Spring</a>

### Contact:
- github: <a href="https://github.com/ibartuszek">github.com/ibartuszek</a>
- email: <a href="mailto:istvan.bartuszek@gmail.com">istvan.bartuszek@gmail.com</a>