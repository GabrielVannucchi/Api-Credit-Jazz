FROM openjdk:17
COPY target/api-analise-credito-0.0.1-SNAPSHOT.jar /app/api-analise-credito.jar
WORKDIR /app
CMD ["java", "-jar", "api-analise-credito.jar"]