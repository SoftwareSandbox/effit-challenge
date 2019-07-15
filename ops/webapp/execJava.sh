#!/bin/sh

# source: https://gist.github.com/wwerner/66ee4b1050bd5824dcfd9a5e0cd07f12#file-heroku-db-url-to-spring-boot-sh-L18

# export DATABASE_URL=postgres://user:password@host:port/database

# Naive way, would break with [@:/] in username or password
DB_TYPE=$(echo $DATABASE_URL | awk -F'[:@/]' '{print $1}')"ql"
DB_USER=$(echo $DATABASE_URL | awk -F'[:@/]' '{print $4}')
DB_PASSWORD=$(echo $DATABASE_URL | awk -F'[:@/]' '{print $5}')
DB_HOST=$(echo $DATABASE_URL | awk -F'[:@/]' '{print $6}')
DB_PORT=$(echo $DATABASE_URL | awk -F'[:@/]' '{print $7}')
DB_DATABASE=$(echo $DATABASE_URL | awk -F'[:@/]' '{print $8}')

export SPRING_DATASOURCE_URL=$(echo "jdbc:$DB_TYPE://$DB_HOST:$DB_PORT/$DB_DATABASE")
export SPRING_DATASOURCE_USERNAME=$DB_USER
export SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD
export SERVER_PORT=$PORT

echo "Parsed database url=$SPRING_DATASOURCE_URL"

java $JAVA_OPTS \
-Djava.security.egd=file:/dev/./urandom -jar \
/app.jar \
