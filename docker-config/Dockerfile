FROM maven AS MAVEN_TOOL
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install -DskipTests

FROM openjdk:19
COPY --from=MAVEN_TOOL /tmp/target/human-resource-department-1.0.0-SNAPSHOT.jar application.jar
ENTRYPOINT ["java","-jar","application.jar"]

HEALTHCHECK --interval=5m --timeout=3s \
  CMD curl -f http://localhost:8080/healthcheck || exit 1