#!/usr/bin/env bash

npm run build
mkdir -p ../effit-cqrs-backend/out/production/resources/static/
rm -rf ../effit-cqrs-backend/out/production/resources/static/*
cp -r dist/* ../effit-cqrs-backend/out/production/resources/static/
ls -lah ../effit-cqrs-backend/out/production/resources/static/
