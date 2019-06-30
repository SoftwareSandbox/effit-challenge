#!/usr/bin/env bash

npm run build
mkdir -p ../effit-backend/out/production/resources/static/
rm -rf ../effit-backend/out/production/resources/static/*
cp -r dist/* ../effit-backend/out/production/resources/static/
ls -lah ../effit-backend/out/production/resources/static/
