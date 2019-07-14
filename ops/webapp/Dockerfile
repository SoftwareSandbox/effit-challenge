FROM adoptopenjdk/openjdk11:jdk-11.0.1.13-alpine-slim
RUN apk add --no-cache curl
RUN apk add --no-cache py-pip
VOLUME /tmp

COPY ./effit-webapp.jar /app.jar

COPY ./execJava.sh /execJava.sh

CMD ./execJava.sh