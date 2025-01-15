FROM eclipse-temurin:17-alpine
COPY ./build/libs/auth-0.0.1.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]