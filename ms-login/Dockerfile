# Build context: Mircoservice root (needs jwt-lib)
FROM eclipse-temurin:21-jdk AS build

WORKDIR /jwt-lib
COPY jwt-lib/ .
RUN ./gradlew publishToMavenLocal --no-daemon -q

WORKDIR /app
COPY ms-login/ .
RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
