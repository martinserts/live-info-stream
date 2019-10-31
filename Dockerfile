# Scala build
FROM hseeberger/scala-sbt:8u222_1.3.3_2.13.1 as scala-build
WORKDIR /app
COPY live-info-stream/ .

# Build fat JAR
RUN sbt assembly

# UI build
FROM node:8-alpine as ui-build
WORKDIR /ui
COPY ui/live-info-stream/ .

RUN npm install && \
    npm install -g @quasar/cli && \
    LIVESTREAM_API_ROOT=https://horse-racing.fplab.info/api/ \
    LIVESTREAM_WS_ROOT=wss://horse-racing.fplab.info/api/ws quasar build

# Runtime
FROM openjdk:8-jre-alpine
WORKDIR /app

COPY --from=scala-build /app/target/scala-2.13/live-info-stream-assembly-0.1.jar .
COPY --from=ui-build /ui/dist/spa/ ./spa/

ENV WEBSERVICE_PORT 8090
ENV WEBSERVICE_ROOT /app/spa

EXPOSE 8090
ENTRYPOINT ["java"]
CMD ["-jar", "/app/live-info-stream-assembly-0.1.jar"]

