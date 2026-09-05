FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Geteiltes Maven-Repo ueber alle Services. Liegt ausserhalb der Layer,
# wird von pom.xml-Aenderungen nicht invalidiert und nie doppelt geladen.
RUN --mount=type=cache,id=maven-repo,target=/root/.m2,sharing=locked \
    mvn package -DskipTests -B

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
