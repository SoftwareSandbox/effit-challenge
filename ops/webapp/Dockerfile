FROM amazoncorretto:17-alpine
VOLUME /tmp

COPY ./effit-ktor.jar /app.jar

CMD java $JAVA_OPTS \
    -Djava.security.egd=file:/dev/./urandom -jar \
    /app.jar
