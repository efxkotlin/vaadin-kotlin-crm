FROM openjdk:8-jre-alpine
ARG JAR_FILE
COPY target/crm-demo-1.0-SNAPSHOT.jar crm.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-Dvaadin.productionMode=true", "-Xmx512m", "-jar", "crm.jar", "--server.port=8080"]
EXPOSE 8080
