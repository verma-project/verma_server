FROM docker.io/maven:3.8-eclipse-temurin-17 AS build

WORKDIR /home/app
COPY . ./

RUN mvn -DskipTests clean package

FROM docker.io/eclipse-temurin:17-jre AS app

ARG VERSION=0.1.0

WORKDIR /home/app
COPY --from=build /home/app/target/repair-cafe-backend-$VERSION.war /home/app/app.war
ENTRYPOINT ["java", "-jar", "/home/app/app.war"]
