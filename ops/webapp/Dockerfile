FROM heroku/heroku:18
#RUN apk add --no-cache curl
#RUN apk add --no-cache py-pip
VOLUME /tmp

COPY ./effit-webapp.jar /app.jar

COPY ./execJava.sh /execJava.sh

CMD ./execJava.sh