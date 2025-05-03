# SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
#
# SPDX-License-Identifier: AGPL-3.0-only

FROM docker.io/eclipse-temurin:17-jdk AS base

FROM base AS build

WORKDIR /home/app
COPY . ./

RUN ./mvnw -DskipTests clean package

FROM base AS app

ARG VERSION=0.1.0

WORKDIR /home/app
COPY --from=build /home/app/target/verma-backend-$VERSION.war /home/app/app.war

ENTRYPOINT ["java", "-jar", "/home/app/app.war"]
