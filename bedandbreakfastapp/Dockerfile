FROM openjdk:21-bookworm AS builder

WORKDIR /src

COPY src src
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY mvnw.cmd .

RUN ./mvnw package -Dmvn.test.skip=true

FROM openjdk:21-bookworm

COPY --from=builder /src/target/assessment-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
ENV SPRING_DATA_MONGODB_URI=NOT_SET 
ENV SPRING_DATASOURCE_URL=localhost 
ENV SPRING_DATASOURCE_USERNAME=default 
ENV SPRING_DATASOURCE_PASSWORD=abc123

EXPOSE $PORT

ENTRYPOINT SERVER_PORT=${PORT} java -jar ./app.jar
