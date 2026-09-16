# Digest-Pin: sonst laedt BuildKit das Image neu, sobald der Tag upstream umzieht.
# Bewusst aktualisieren mit: docker buildx imagetools inspect maven:3.9-eclipse-temurin-21
FROM maven:3.9-eclipse-temurin-21@sha256:a972570be789ee5c9fa23446a8914ac7327560b5c022f662cfa9452aef829f18 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Geteiltes Maven-Repo ueber alle Services. Liegt ausserhalb der Layer,
# wird von pom.xml-Aenderungen nicht invalidiert und nie doppelt geladen.
RUN --mount=type=cache,id=maven-repo,target=/root/.m2,sharing=locked \
    mvn package -DskipTests -B

# Digest-Pin, siehe oben.
FROM eclipse-temurin:21-jre@sha256:a80c51f2d09a3e7e00d521f1c817bbceb6b3be94109b4a784d46078099882dda
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
