FROM adoptopenjdk/openjdk11:jdk-11.0.1.13-alpine-slim
RUN apk add --no-cache curl
RUN apk add --no-cache py-pip
VOLUME /tmp

COPY ./effit-cqrs-webapp.jar /app.jar

#COPY ./execJava.sh /execJava.sh

CMD java $JAVA_OPTS \
    -Djava.security.egd=file:/dev/./urandom -jar \
    /app.jar
