# Playground:
The purpose of this project is to try-out different technologies and frameworks with java-based
spring-boot web-application. They are separated into different packages and different commits.
 - spring web-flux (webclient)
 - open-api 3 (openapi)
 - mongodb

### Sample Usage
User can use the application via:
<a href=http://localhost:8080/swagger-ui.html#/>localhost:8080/swagger-ui.html</a>

### Dependencies:
 - Spring-boot
 - org.springframework.boot:spring-boot-starter-webflux
 - org.springdoc:springdoc-openapi-ui
 - org.springdoc:springdoc-openapi-data-rest
 - org.springframework.boot:spring-boot-starter-data-mongodb
 - de.flapdoodle.embed:de.flapdoodle.embed.mongo

## Build:
 - clone from github: `git clone git@github.com:ibartuszek/playground.git`
 - `./mvnw clean package` at the main folder of the project

## Run:
 - before running the application:
   - run mongodb on localhost:27017
     
        with docker: e.g.: `docker run --name playground-mongodb -p 27017:27017 -d mongo:latest`
 - `./mvnw spring-boot:run` at the main folder of the project to start application. This case you can reach swagger on: 
   <a href=http://localhost:8080/swagger-ui.html#/>localhost:8080</a> port.

### Useful links
- <a href="https://github.com/ibartuszek/playground">playground github repo</a>
- <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/">Spring boot documentation</a>
- <a href="https://springdoc.org">springdoc.org</a>
- <a href="https://robomongo.org/">robomongodb.org<a/>

### Contact:
- github: <a href="https://github.com/ibartuszek">github.com/ibartuszek</a>
- email: <a href="mailto:istvan.bartuszek@gmail.com">istvan.bartuszek@gmail.com</a>