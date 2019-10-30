# Scala build
FROM hseeberger/scala-sbt:11.0.10_1.4.7_2.13.4 as scala-build
WORKDIR /app
COPY live-info-stream/ .

# Build fat JAR
RUN sbt ";build;assembly"

# UI build
FROM node:15-alpine as ui-build
WORKDIR /ui
COPY ui/live-info-stream/ .

RUN apk add --no-cache --virtual .gyp \
        python \
        make \
        g++ && \
    npm install && \
    npm install -g @quasar/cli && \
    LIVESTREAM_CHARTS_URL='https://grafana.fplab.info/d/WiWvxiyMk/horse-race?orgId=2&from=$FROM&to=$TO&var-market_id=$MARKET_ID&var-runner_name=All&refresh=10s' \
    LIVESTREAM_API_ROOT=https://horse-racing.fplab.info/api/ \
    LIVESTREAM_WS_ROOT=wss://horse-racing.fplab.info/api/ws quasar build

# Runtime
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app

COPY --from=scala-build /app/target/scala-2.13/live-info-stream-assembly-0.1.jar .
COPY --from=ui-build /ui/dist/spa/ ./spa/

ENV WEBSERVICE_PORT 8090
ENV WEBSERVICE_ROOT /app/spa

EXPOSE 8090
ENTRYPOINT ["java"]
CMD ["-Dorg.slf4j.simpleLogger.defaultLogLevel=debug", "-jar", "/app/live-info-stream-assembly-0.1.jar"]

