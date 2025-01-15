FROM eclipse-temurin:17-alpine
COPY ./build/libs/payment-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]