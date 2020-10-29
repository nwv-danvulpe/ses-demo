FROM openjdk:11-slim as builder

WORKDIR /app

COPY . .

RUN ./gradlew bootJar

FROM amazoncorretto:11-alpine

RUN mkdir /app

COPY --from=builder /app/build/libs/ses-demo-1.0.jar /app/ses-demo-1.0.jar

ENTRYPOINT [ "java", "-jar", "/app/ses-demo-1.0.jar" ]