FROM eclipse-temurin:17-alpine
COPY ./build/libs/gateway-0.0.1.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]