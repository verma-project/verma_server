FROM docker.io/eclipse-temurin:17-jre AS base

FROM base AS build

WORKDIR /home/app
COPY . ./

RUN ./mvnw -DskipTests clean package

FROM base AS app

ARG VERSION=0.1.0

WORKDIR /home/app
COPY --from=build /home/app/target/dera-backend-$VERSION.war /home/app/app.war

ENTRYPOINT ["java", "-jar", "/home/app/app.war"]
